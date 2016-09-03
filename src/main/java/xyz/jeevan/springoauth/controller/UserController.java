package xyz.jeevan.springoauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import xyz.jeevan.springoauth.domain.User;
import xyz.jeevan.springoauth.service.UserService;
import xyz.jeevan.springoauth.util.Endpoints;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:26:58 pm
 * @purpose
 *
 */
@Controller
@RequestMapping(value = Endpoints.USER_API_URL)
public class UserController {
	private static final org.slf4j.Logger _log = org.slf4j.LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * API to save new user
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> save(@RequestBody User user) {
		_log.info("Creating new user with username: " + user.getUsername());
		User savedUser = userService.create(user);
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}

	/**
	 * API to fetch list of users matching the criteria provided.
	 * 
	 * @param value
	 * @param field
	 * @return
	 */
	@RequestMapping(value = "/uname/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getByFieldAndValue(@PathVariable String username) {
		return new ResponseEntity<User>(userService.getByUsername(username), HttpStatus.OK);
	}
}
