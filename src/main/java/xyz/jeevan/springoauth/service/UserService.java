package xyz.jeevan.springoauth.service;

import java.util.List;

import xyz.jeevan.springoauth.domain.User;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:03:40 pm
 * @purpose User services.
 *
 */
public interface UserService {

	/**
	 * Create new user in the system.
	 * 
	 * @param user
	 * @return
	 */
	User create(User user);

	/**
	 * Update existing user details.
	 * 
	 * @param user
	 * @return
	 */
	User update(User user);

	/**
	 * Get user details by guid.
	 * 
	 * @param guid
	 * @return
	 */
	User getByGuid(String guid);

	/**
	 * Delete existing user.
	 * 
	 * @param guid
	 */
	void delete(String guid);

	/**
	 * Fetch user data by username.
	 * 
	 * @param username
	 * @return
	 */
	User getByUsername(String username);

	/**
	 * Method to fetch user list with matching values for field provided.
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	List<User> getByField(String field, String value);

	/**
	 * Method to validate username provided by user while registration process.
	 * 
	 * @param username
	 * @return
	 */
	public boolean validateUsername(String username);

	/**
	 * Method to validate email provided by user while registration process.
	 * 
	 * @param email
	 * @return
	 */
	public boolean validateEmail(String email);

}
