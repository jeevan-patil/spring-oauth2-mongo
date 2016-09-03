package xyz.jeevan.springoauth.domain.oauth;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 10:37:42 pm
 * @purpose This domain class will represent our access token in the mongo db
 *
 */
@Document(collection = "accessTokens")
public class AccessToken extends BaseEntity {
	private String tokenId;
	private OAuth2AccessToken oAuth2AccessToken;
	private String authenticationId;
	private String userName;
	private String clientId;
	private OAuth2Authentication authentication;
	private String refreshToken;

	public AccessToken() {
	}

	public AccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication,
			final String authenticationId) {
		this.tokenId = oAuth2AccessToken.getValue();
		this.oAuth2AccessToken = oAuth2AccessToken;
		this.authenticationId = authenticationId;
		this.userName = authentication.getName();
		this.clientId = authentication.getOAuth2Request().getClientId();
		this.authentication = authentication;
		this.refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
	}

	public String getTokenId() {
		return tokenId;
	}

	public OAuth2AccessToken getoAuth2AccessToken() {
		return oAuth2AccessToken;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public String getUserName() {
		return userName;
	}

	public String getClientId() {
		return clientId;
	}

	public OAuth2Authentication getAuthentication() {
		return authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

}
