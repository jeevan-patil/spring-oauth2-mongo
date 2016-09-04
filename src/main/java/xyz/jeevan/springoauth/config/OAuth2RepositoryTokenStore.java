package xyz.jeevan.springoauth.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import xyz.jeevan.springoauth.domain.oauth.AccessToken;
import xyz.jeevan.springoauth.domain.oauth.RefreshToken;
import xyz.jeevan.springoauth.repository.oauth.AccessTokenRepository;
import xyz.jeevan.springoauth.repository.oauth.RefreshTokenRepository;

/**
 * 
 * @author jeevan
 * @date 04-Sep-2016 10:39:43 pm
 * @purpose Since we are using MongoDB for storing tokens, we need to provide
 *          custom implementation for access and refresh token operations. In
 *          case of MySql, it's not needed.
 *
 */
@Component("tokenStore")
public class OAuth2RepositoryTokenStore implements TokenStore {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OAuth2RepositoryTokenStore.class);

	private final AccessTokenRepository accessTokenRepository;

	private final RefreshTokenRepository refreshTokenRepository;

	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	@Autowired
	public OAuth2RepositoryTokenStore(final AccessTokenRepository AccessTokenRepository,
			final RefreshTokenRepository RefreshTokenRepository) {
		this.accessTokenRepository = AccessTokenRepository;
		this.refreshTokenRepository = RefreshTokenRepository;
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		log.info("reading authentication token");
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String tokenId) {
		log.info("read authentication token id: " + tokenId);
		return accessTokenRepository.findByTokenId(tokenId).getAuthentication();
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		log.info("storing access token");
		AccessToken AccessToken = new AccessToken(token, authentication,
				authenticationKeyGenerator.extractKey(authentication));
		final String tokenId = AccessToken.getTokenId();
		OAuth2AccessToken accessToken = readAccessToken(tokenId);
		if (accessToken == null) {
			accessTokenRepository.save(AccessToken);
		}
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		log.info("read access token: " + tokenValue);
		AccessToken token = accessTokenRepository.findByTokenId(tokenValue);
		if (token == null) {
			return null; // let spring security handle the invalid token
		}
		return token.getoAuth2AccessToken();
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		log.info("removing access token");
		AccessToken accessToken = accessTokenRepository.findByTokenId(token.getValue());
		if (accessToken != null) {
			accessTokenRepository.delete(accessToken);
		}
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		log.info("storing refresh token");
		refreshTokenRepository.save(new RefreshToken(refreshToken, authentication));
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		log.info("reading refresh token id : " + tokenValue);
		RefreshToken refreshToken = refreshTokenRepository.findByTokenId(tokenValue);
		if (refreshToken != null) {
			return refreshToken.getoAuth2RefreshToken();
		}

		return null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		log.info("reading authentication for refresh token");
		return refreshTokenRepository.findByTokenId(token.getValue()).getAuthentication();
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		log.info("remove refresh token");
		RefreshToken refreshToken = refreshTokenRepository.findByTokenId(token.getValue());
		if (refreshToken != null) {
			refreshTokenRepository.delete(refreshToken);
		}
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		log.info("remove access token using refresh token");
		AccessToken accessToken = accessTokenRepository.findByRefreshToken(refreshToken.getValue());
		if (accessToken != null) {
			accessTokenRepository.delete(accessToken);
		}
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		log.info("get access token for user: " + authentication.getPrincipal());
		AccessToken token = accessTokenRepository
				.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
		return token == null ? null : token.getoAuth2AccessToken();
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		log.info("find tokens by client id");
		List<AccessToken> tokens = accessTokenRepository.findByClientId(clientId);
		return extractAccessTokens(tokens);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		log.info("find tokens by client id and username");
		List<AccessToken> tokens = accessTokenRepository.findByClientIdAndUserName(clientId, userName);
		return extractAccessTokens(tokens);
	}

	private Collection<OAuth2AccessToken> extractAccessTokens(List<AccessToken> tokens) {
		log.info("extract access tokens");
		List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
		for (AccessToken token : tokens) {
			accessTokens.add(token.getoAuth2AccessToken());
		}
		return accessTokens;
	}
}
