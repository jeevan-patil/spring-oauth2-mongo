package xyz.jeevan.springoauth.advice;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jeevan.springoauth.domain.ErrorResponse;
import xyz.jeevan.springoauth.exception.ApplicationException;
import xyz.jeevan.springoauth.exception.ErrorResponseEnum;
import xyz.jeevan.springoauth.exception.ValidationException;

@ControllerAdvice(basePackages = { "xyz.jeevan.springoauth" })
public class ExceptionHandlerAdvice {

	private static final org.slf4j.Logger _log = org.slf4j.LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	@ExceptionHandler({ ValidationException.class })
	public @ResponseBody ResponseEntity<ErrorResponse> handleValidationException(
			ValidationException ValidationException) {
		_log.warn("ValidationException is thrown : ");
		// Create error response object.
		ErrorResponse errorResponse = new ErrorResponse(ValidationException.getErrorResponse().getCode(),
				ValidationException.getErrorResponse().getErrorText(), ValidationException.getValidationError(),
				ValidationException.getValidationErrorList());

		_log.warn(errorResponse.toString());
		// Return error response with status.
		return new ResponseEntity<ErrorResponse>(errorResponse, ValidationException.getErrorResponse().getHttpStatus());
	}

	@ExceptionHandler({ ApplicationException.class })
	public @ResponseBody ResponseEntity<ErrorResponse> handleApplicationException(
			ApplicationException ApplicationException) {
		_log.warn("RuntimeException is thrown : ");

		// Create error response object.
		ErrorResponse errorResponse = new ErrorResponse(ApplicationException.getErrorResponse().getCode(),
				ApplicationException.getErrorResponse().getErrorText());

		_log.warn(errorResponse.toString());
		// Return error response with status.
		return new ResponseEntity<ErrorResponse>(errorResponse,
				ApplicationException.getErrorResponse().getHttpStatus());
	}

	@ExceptionHandler({ NotFoundException.class })
	public @ResponseBody ResponseEntity<ErrorResponse> handleNotFoundException(Throwable throwable) {
		_log.warn(throwable.getMessage(), throwable);

		ErrorResponse errorResponse = new ErrorResponse(ErrorResponseEnum.INVALID_URL.getCode(),
				ErrorResponseEnum.INVALID_URL.getErrorText());
		_log.error(errorResponse.toString());

		return new ResponseEntity<ErrorResponse>(errorResponse, ErrorResponseEnum.INVALID_URL.getHttpStatus());
	}

	@ExceptionHandler({ Throwable.class })
	public @ResponseBody ResponseEntity<ErrorResponse> handleException(Throwable throwable) {
		_log.error(throwable.getMessage(), throwable);

		ErrorResponse errorResponse = new ErrorResponse(ErrorResponseEnum.GENERAL_ERROR.getCode(),
				ErrorResponseEnum.GENERAL_ERROR.getErrorText());
		_log.error(errorResponse.toString());

		return new ResponseEntity<ErrorResponse>(errorResponse, ErrorResponseEnum.GENERAL_ERROR.getHttpStatus());
	}
}
