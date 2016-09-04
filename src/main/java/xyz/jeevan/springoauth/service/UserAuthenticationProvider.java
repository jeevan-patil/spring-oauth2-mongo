package xyz.jeevan.springoauth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import xyz.jeevan.springoauth.domain.User;
import xyz.jeevan.springoauth.repository.UserRepository;
import xyz.jeevan.springoauth.util.SecurityUtil;

/**
 * 
 * @author jeevan
 * @date 04-Sep-2016 10:51:49 pm
 * @purpose
 *
 */
@Component
public class UserAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	@Autowired
	private UserRepository userRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String userName = authentication.getName();
		final String password = (String) authentication.getCredentials();

		User user = userRepo.findByUsername(userName);
		if (null == user || !SecurityUtil.passwordMatches(user.getPassword(), password)) {
			throw new BadCredentialsException("Invalid Username or Password.");
		}

		// check if account is active or not
		if (!user.isActive()) {
			throw new BadCredentialsException("User account is deactivated.");
		}

		// in the following line, send back the encrypted password, because we
		// have not configured password encoder mechanism
		return new UsernamePasswordAuthenticationToken(userName, user.getPassword(), getAuthorities(user));
	}

	/**
	 * Derive authorities of user. Add DEFAULT_ROLE to every user, however this
	 * role is not stored in the user collection.
	 * 
	 * @param user
	 * @return
	 */
	private List<GrantedAuthority> getAuthorities(final User user) {
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority(user.getRole()));
		return grantedAuths;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}
