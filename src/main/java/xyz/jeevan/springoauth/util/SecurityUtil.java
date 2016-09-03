package xyz.jeevan.springoauth.util;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 9:55:48 pm
 * @purpose
 *
 */
public class SecurityUtil {

	public static StandardPasswordEncoder encoder() {
		return new StandardPasswordEncoder();
	}

	public static String encode(final String pass) {
		return (StringUtils.isEmpty(pass) ? null : encoder().encode(pass));
	}
}
