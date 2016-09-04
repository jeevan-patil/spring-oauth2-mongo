package xyz.jeevan.springoauth.converters;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

/**
 * 
 * @author jeevan
 * @date 04-Sep-2016 11:15:51 pm
 * @purpose MongoDB can not convert DBObject to OAuth2Authentication object. We
 *          need to write converter which reads the object data and prepare
 *          OAuth2Authentication object.
 *
 */
@Component
@ReadingConverter
public class OAuthAuthenticationReadConverter implements Converter<DBObject, OAuth2Authentication> {

	private static Logger logger = LoggerFactory.getLogger(OAuthAuthenticationReadConverter.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public OAuth2Authentication convert(DBObject source) {
		logger.info("Authenticating in custom converrter");

		DBObject storedRequest = (DBObject) source.get("storedRequest");
		OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
				(String) storedRequest.get("clientId"), null, true, new HashSet((List) storedRequest.get("scope")),
				null, null, null, null);
		DBObject userAuthorization = (DBObject) source.get("userAuthentication");

		logger.info("User Authorization object: " + userAuthorization);

		String principal = (String) userAuthorization.get("principal");
		logger.info("Authenticating in custom converrter for user id: " + principal);

		Authentication userAuthentication = new UsernamePasswordAuthenticationToken(principal, null,
				getAuthorities((List) userAuthorization.get("authorities")));

		OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
		authentication.setAuthenticated(true);
		return authentication;
	}

	private Collection<GrantedAuthority> getAuthorities(List<Map<String, String>> authorities) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>(authorities.size());
		for (Map<String, String> authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.get("role")));
		}
		return grantedAuthorities;
	}

}
