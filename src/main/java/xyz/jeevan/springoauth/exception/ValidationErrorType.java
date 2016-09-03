package xyz.jeevan.springoauth.exception;

import org.apache.commons.lang.StringUtils;

import xyz.jeevan.springoauth.util.AppConstants;

public enum ValidationErrorType {
	REQUIRED_FIELD_MISSING("REQUIRED_FIELD_MISSING"),
	INVALID_VALUE("INVALID_VALUE"),
	MULTIPLE_CONTENT_OF_SAME_LANG("MULTIPLE_CONTENT_OF_SAME_LANG"),
	VALUE_ALREADY_IN_USE("VALUE_ALREADY_IN_USE"),
	USERNAME_TAKEN("USERNAME_TAKEN"),
	EMAIL_EXISTS("EMAIL_EXISTS"),
	INVALID_ROLE("INVALID_ROLE. Must be any of these [" + StringUtils.join(AppConstants.ROLE_LIST, ",") + "].");

	ValidationErrorType(String errorType) {
		this.errorType = errorType;
	}

	private String errorType;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
}
