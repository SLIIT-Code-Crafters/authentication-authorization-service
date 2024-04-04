package com.sep.authenticationauthorization.configuration.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.authenticationauthorization.configuration.dto.login.LoginRequest;
import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.service.LoginService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/users")
public class LoginController {

	@Autowired
	private LoginService service;

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

		String userInput = CommonUtils.isUserNameOrEmail(loginRequest.getEmail());

		Boolean success = service.login(userInput, loginRequest.getEmail(), loginRequest.getPassword(), requestId);

		if (success) {
			response.setSuccess(true);
			response.setMessage("User Login Successfull");
			response.setStatus(TSMSError.OK.getStatus());
		} else {
			response.setSuccess(false);
			response.setMessage("User Login Failed");
			response.setStatus(TSMSError.FAILED.getStatus());
		}

		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] authenticate: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return ResponseEntity.ok(response);
	}

}
