package com.sep.authenticationauthorization.configuration.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = TSMSException.class)
	public ResponseEntity<TSMSResponse> handleGlobleException(TSMSException dcnpException) {

		TSMSResponse response = new TSMSResponse();
		response.setSuccess(true);
		response.setStatus(dcnpException.getError().getStatus());
		response.setCode(dcnpException.getError().getCode());
		response.setMessage(dcnpException.getError().getMessage());

		return ResponseEntity.status(dcnpException.getError().getStatus()).body(response);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<TSMSResponse> handleGlobleException(RuntimeException e) {

		logger.error("Exception occur  : {}", e.getMessage(), e);

		TSMSResponse response = new TSMSResponse();
		response.setSuccess(true);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<TSMSResponse> handleGlobleException(Exception e) {

		logger.error("Exception occur  : {}", e.getMessage(), e);

		TSMSResponse response = new TSMSResponse();
		response.setSuccess(true);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<TSMSResponse> handleGlobleException(AccessDeniedException e) {
//
//		logger.error("Exception occur  3: {}", e.getMessage(), e);
//
//		String errorMessage = "Access to the requested resource is forbidden.";
//
//		TSMSResponse response = new TSMSResponse();
//		response.setSuccess(true);
//		response.setStatus(HttpStatus.BAD_REQUEST.value());
//		response.setMessage(e.getMessage());
//
//		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
//	}
//
//	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//	@ResponseBody
//	public ResponseEntity<String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
//		// Customize the message here
//		String errorMessage = "The request method is not allowed for the requested endpoint.";
//
//		// Return a ResponseEntity with a custom message and 405 status code
//		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorMessage);
//	}
//
//	@ExceptionHandler(AuthenticationException.class)
//	public ResponseEntity<String> handleUnauthorizedException(AuthenticationException ex) {
//		// Customize the message here
//		String errorMessage = "Authentication is required to access this resource.";
//
//		// Return a ResponseEntity with a custom message and 401 status code
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
//	}

	

}
