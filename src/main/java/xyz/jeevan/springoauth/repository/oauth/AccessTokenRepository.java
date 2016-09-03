package xyz.jeevan.springoauth.repository.oauth;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xyz.jeevan.springoauth.domain.oauth.AccessToken;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:01:42 pm
 * @purpose
 *
 */
@Repository
public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {
	public AccessToken findByTokenId(String tokenId);

	public AccessToken findByRefreshToken(String refreshToken);

	public AccessToken findByAuthenticationId(String authenticationId);

	public List<AccessToken> findByClientIdAndUserName(String clientId, String userName);

	public List<AccessToken> findByClientId(String clientId);
}
