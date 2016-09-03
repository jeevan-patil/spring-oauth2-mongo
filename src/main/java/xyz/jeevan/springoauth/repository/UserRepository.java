package xyz.jeevan.springoauth.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import xyz.jeevan.springoauth.domain.User;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 10:47:52 pm
 * @purpose
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public User findByGuid(String guid);

	@Query("{'username' : { $regex: ?0, $options: 'i'}}")
	public User findByUsername(String username);

	@Query("{?0 : { $regex: ?1, $options: 'i'}}")
	public List<User> findByField(String field, String value);
}
