package xyz.jeevan.springoauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import xyz.jeevan.springoauth.util.AppConstants;

/**
 * 
 * @author jeevan
 * @date 04-Sep-2016 10:54:19 pm
 * @purpose
 *
 */
@Configuration
@EnableAuthorizationServer
@Order(5)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
	@Autowired
	@Qualifier("tokenStore")
	private TokenStore tokenStore;

	@Autowired
	@Qualifier("CustomAuthenticationManagerBean")
	private AuthenticationManager authenticationManager;

	private final String CLIENT_ID = "vYkFSKJhqUqlIRosdfwJFru";
	private final String CLIENT_SECRET = "cmuF7GgqBmWDqVXJHSksd";

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		String[] authorities = new String[AppConstants.ROLE_LIST.size()];
		authorities = AppConstants.ROLE_LIST.toArray(authorities);

		clients.inMemory().withClient(CLIENT_ID)
				.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
				.authorities(authorities).scopes("read", "write", "trust").secret(CLIENT_SECRET)
				.accessTokenValiditySeconds(12000).refreshTokenValiditySeconds(60000);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

}
