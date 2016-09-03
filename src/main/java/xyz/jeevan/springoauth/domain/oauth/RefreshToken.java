package xyz.jeevan.springoauth.domain.oauth;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 10:38:37 pm
 * @purpose
 *
 */
@Document(collection = "refreshTokens")
public class RefreshToken extends BaseEntity {

	private String tokenId;
	private OAuth2RefreshToken oAuth2RefreshToken;
	private OAuth2Authentication authentication;

	public RefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
		this.oAuth2RefreshToken = oAuth2RefreshToken;
		this.authentication = authentication;
		this.tokenId = oAuth2RefreshToken.getValue();
	}

	public String getTokenId() {
		return tokenId;
	}

	public OAuth2RefreshToken getoAuth2RefreshToken() {
		return oAuth2RefreshToken;
	}

	public OAuth2Authentication getAuthentication() {
		return authentication;
	}
}
