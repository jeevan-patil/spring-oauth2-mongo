package xyz.jeevan.springoauth.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author jeevan
 * @date 03-Sep-2016 11:13:19 pm
 * @purpose Exception / Error messages repository.
 *
 */
public enum ErrorResponseEnum {
	GENERAL_ERROR(100, "An exception has occured while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR),
	VALIDATION_ERROR(101, "There was one or more validation error(s)", HttpStatus.BAD_REQUEST),
	INVALID_URL(102, "Invalid url, request not found", HttpStatus.NOT_FOUND),
	ENTITY_NOT_FOUND(103, "The requested entity could not be found", HttpStatus.BAD_REQUEST),
	FILE_UPLOAD_ERROR(104, "An exception has occured while uploading file.", HttpStatus.BAD_REQUEST),
	AMAZON_FILE_UPLOAD_SERVICE_ERROR(105, "An exception has occured while uploading file to amazon S3.", HttpStatus.BAD_REQUEST),
	METHOD_NOT_AVAILABLE(106, "This method is not supported.", HttpStatus.BAD_REQUEST),
	USER_ALREADY_EXISTS(107, "Username is already taken.", HttpStatus.BAD_REQUEST),
	EMAIL_ALREADY_EXISTS(107, "Email address already exists.", HttpStatus.BAD_REQUEST),
	METHOD_NOT_IMPLEMENTED(108, "Method not implemented yet.", HttpStatus.METHOD_NOT_ALLOWED);

	private int code;
	private String errorText;
	private HttpStatus httpStatus;

	private ErrorResponseEnum(int code, String errorText, HttpStatus httpStatus) {
		this.code = code;
		this.errorText = errorText;
		this.httpStatus = httpStatus;
	}

	public int getCode() {
		return code;
	}

	public String getErrorText() {
		return errorText;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
