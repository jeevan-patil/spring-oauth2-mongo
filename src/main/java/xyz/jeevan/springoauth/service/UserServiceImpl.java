package xyz.jeevan.springoauth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.jeevan.springoauth.domain.User;
import xyz.jeevan.springoauth.exception.ErrorResponseEnum;
import xyz.jeevan.springoauth.exception.ValidationError;
import xyz.jeevan.springoauth.exception.ValidationErrorType;
import xyz.jeevan.springoauth.exception.ValidationException;
import xyz.jeevan.springoauth.repository.UserRepository;
import xyz.jeevan.springoauth.util.AppConstants;
import xyz.jeevan.springoauth.util.CollectionUtil;
import xyz.jeevan.springoauth.util.SecurityUtil;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:05:58 pm
 * @purpose
 *
 */

@Service
public class UserServiceImpl implements UserService {

	private static final org.slf4j.Logger _log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Override
	public User create(User user) {
		_log.debug("Creating new user with username: " + user.getUsername());

		// server side validation to check if required fields are missing
		List<ValidationError> validationErrorList = this.validateUserBeforeSave(user);
		if (!validationErrorList.isEmpty()) {
			throw new ValidationException(validationErrorList, ErrorResponseEnum.VALIDATION_ERROR);
		}

		String username = user.getUsername();
		validateUsername(username);

		String email = user.getEmail();
		validateEmail(email);

		String encryptedPassword = SecurityUtil.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		user.setActive(true);
		user.setGuid(null);
		return userRepo.save(user);
	}

	@Override
	public User update(User user) {
		throw new ValidationException(new ValidationError("method", "Method not implemented."),
				ErrorResponseEnum.VALIDATION_ERROR);
	}

	@Override
	public User getByGuid(String guid) {
		User user = userRepo.findByGuid(guid);

		if (null == user) {
			throw new ValidationException(
					new ValidationError(AppConstants.GUID, ValidationErrorType.INVALID_VALUE.toString()),
					ErrorResponseEnum.ENTITY_NOT_FOUND);
		}

		return user;
	}

	@Override
	public void delete(String guid) {
		_log.debug("Deleting user with guid: " + guid);
		User user = this.getByGuid(guid);
		if (user.isActive()) {
			user.setActive(false);
			userRepo.save(user);
		}
	}

	@Override
	public User getByUsername(String username) {
		User user = userRepo.findByUsername(username);

		if (null == user) {
			throw new ValidationException(
					new ValidationError(AppConstants.USERNAME, ValidationErrorType.INVALID_VALUE.toString()),
					ErrorResponseEnum.ENTITY_NOT_FOUND);
		}

		return user;
	}

	@Override
	public List<User> getByField(String field, String value) {
		List<User> users = userRepo.findByField(field, value);

		if (CollectionUtil.isEmpty(users)) {
			throw new ValidationException(new ValidationError(field, ValidationErrorType.INVALID_VALUE.toString()),
					ErrorResponseEnum.ENTITY_NOT_FOUND);
		}

		return users;
	}

	@Override
	public boolean validateUsername(String username) {
		User user = userRepo.findByUsername(username);

		if (null != user) {
			throw new ValidationException(
					new ValidationError(AppConstants.USERNAME, ValidationErrorType.USERNAME_TAKEN.toString()),
					ErrorResponseEnum.USER_ALREADY_EXISTS);
		}

		return true;
	}

	@Override
	public boolean validateEmail(String email) {
		List<User> usersWithEmail = userRepo.findByField("email", email);

		if (!CollectionUtil.isEmpty(usersWithEmail)) {
			throw new ValidationException(
					new ValidationError(AppConstants.EMAIL, ValidationErrorType.EMAIL_EXISTS.toString()),
					ErrorResponseEnum.EMAIL_ALREADY_EXISTS);
		}

		return true;
	}

	/**
	 * Method to validate user details before saving in the database. The fields
	 * we are checking for validation need to be in sync with UI validations.
	 * 
	 * @param user
	 * @return
	 */
	private List<ValidationError> validateUserBeforeSave(final User user) {
		List<ValidationError> validationErrorList = new ArrayList<ValidationError>();

		if (StringUtils.isEmpty(user.getUsername())) {
			validationErrorList.add(new ValidationError(AppConstants.USERNAME,
					ValidationErrorType.REQUIRED_FIELD_MISSING.getErrorType()));
		}

		if (StringUtils.isEmpty(user.getEmail())) {
			validationErrorList.add(
					new ValidationError(AppConstants.EMAIL, ValidationErrorType.REQUIRED_FIELD_MISSING.getErrorType()));
		}

		if (StringUtils.isEmpty(user.getName())) {
			validationErrorList.add(
					new ValidationError(AppConstants.NAME, ValidationErrorType.REQUIRED_FIELD_MISSING.getErrorType()));
		}

		if (StringUtils.isEmpty(user.getRole())) {
			validationErrorList.add(
					new ValidationError(AppConstants.ROLE, ValidationErrorType.REQUIRED_FIELD_MISSING.getErrorType()));
		}

		if (!AppConstants.ROLE_LIST.contains(user.getRole())) {
			validationErrorList
					.add(new ValidationError(AppConstants.ROLE, ValidationErrorType.INVALID_ROLE.getErrorType()));
		}

		return validationErrorList;
	}
}
