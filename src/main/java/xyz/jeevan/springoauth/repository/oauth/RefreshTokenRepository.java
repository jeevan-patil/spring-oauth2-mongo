package xyz.jeevan.springoauth.repository.oauth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xyz.jeevan.springoauth.domain.oauth.RefreshToken;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:01:38 pm
 * @purpose
 *
 */
@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

	public RefreshToken findByTokenId(String tokenId);
}
