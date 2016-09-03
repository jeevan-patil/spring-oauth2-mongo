package xyz.jeevan.springoauth.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import xyz.jeevan.springoauth.domain.User;
import xyz.jeevan.springoauth.util.AppConstants;
import xyz.jeevan.springoauth.util.SecurityUtil;

@Component
public class PostConstructInitializer {
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PostConstructInitializer.class);

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void addCollectionindex() {
		LOGGER.info("Create collection or Add index on collection if not exist.");

		if (!mongoTemplate.collectionExists(User.class)) {
			mongoTemplate.createCollection(User.class);
			mongoTemplate.insert(createAdminUser(), User.class);
		}
	}

	private List<User> createAdminUser() {
		List<User> users = new ArrayList<User>();
		User user = new User();
		user.setGuid(null);
		user.setUsername("admin");
		user.setActive(true);
		user.setEmail("admin@jeevan.xyz");
		user.setName("Jeevan Patil");
		user.setRole(AppConstants.ROLE_ADMIN);
		user.setPassword(SecurityUtil.encode("patil"));
		user.setMobile("23746786324");
		user.setState("MH");
		user.setCountry("IN");
		user.setCompany("Patil INC.");
		users.add(user);
		return users;
	}
}
