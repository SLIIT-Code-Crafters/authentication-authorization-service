package com.sep.authenticationauthorization.configuration.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.authenticationauthorization.configuration.dto.login.LoginRequest;
import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;
import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/users")
public class LoginController {

	@Autowired
	private AuthenticationService service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	
	@PostMapping("/login")
	public ResponseEntity<TSMSResponse> login(@RequestParam("requestId") String requestId,
			@RequestBody LoginRequest loginRequest) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] login: request={}", requestId,
				CommonUtils.convertToString(loginRequest));

		TSMSResponse response = new TSMSResponse();
		response.setRequestId(requestId);

		if (!CommonUtils.checkUserInputNullOrEmpty(loginRequest)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] login : Mandatory fields are null. Please ensure all required fields are provided",
					requestId);
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		String userInput = CommonUtils.isUserNameOrEmail(loginRequest.getUser());
		String password = "";

		if (userInput.equals("EMAIL")) {

			// Service Call.
			Optional<User> user = service.getByEmail(loginRequest.getUser(), requestId);
			if (user.isEmpty()) {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] login : Invalid Email or User Not Found", requestId);
				throw new TSMSException(TSMSError.INVALID_EMAIL_OR_USER_NOT_FOUND);
			} else {
				password = user.get().getPassword();
			}

		} else if (userInput.equals("USERNAME")) {

			// Service Call.
			User user = service.getByUserName(loginRequest.getUser(), requestId);
			if (user != null) {
				password = user.getPassword();
			} else {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] login : Invalid Username or User Not Found", requestId);
				throw new TSMSException(TSMSError.INVALID_USERNAME_OR_USER_NOT_FOUND);
			}

		} else {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] login : Invalid Email Address or Username", requestId);
			throw new TSMSException(TSMSError.INVALID_EMAIL_USERNAME);
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), password)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] login : Password incorrect. Please verify your password and try again",
					requestId);
			throw new TSMSException(TSMSError.INCORRECT_PASSWORD);
		}

		response.setSuccess(true);
		response.setMessage("User Login Successfull");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] authenticate: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return ResponseEntity.ok(response);
	}
}
