package xyz.jeevan.springoauth.controller;

import static org.springframework.http.HttpStatus.OK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		logger.info("Welcome home!");
		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ResponseEntity<String> index() {
		logger.info("Index Page!");
		return new ResponseEntity<String>("Spring OAuth MongoDB", OK);
	}

}
