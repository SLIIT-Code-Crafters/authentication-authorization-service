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

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

		LOGGER.error("Exception occur  : {}", e.getMessage(), e);

		TSMSResponse response = new TSMSResponse();
		response.setSuccess(true);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<TSMSResponse> handleGlobleException(Exception e) {

		LOGGER.error("Exception occur  : {}", e.getMessage(), e);

		TSMSResponse response = new TSMSResponse();
		response.setSuccess(true);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setMessage(e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
