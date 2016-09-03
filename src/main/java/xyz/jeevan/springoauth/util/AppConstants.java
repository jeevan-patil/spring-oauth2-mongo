package xyz.jeevan.springoauth.util;

import java.util.ArrayList;
import java.util.List;

public final class AppConstants {
	public static final String GUID = "guid";
	public static final String NAME = "name";
	public static final String SUCCESS = "Success";
	public static final String EMPTY_REQUEST = "empty_request";
	public static final String STATUS = "status";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String ROLE = "role";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	public static final List<String> ROLE_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -8950161359099679478L;
		{
			add(ROLE_ADMIN);
			add(ROLE_USER);
		}
	};
}
