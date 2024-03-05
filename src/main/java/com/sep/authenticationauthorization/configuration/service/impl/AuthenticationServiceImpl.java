package com.sep.authenticationauthorization.configuration.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.enums.Status;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.repository.UserRepository;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.service.JwtService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Override
	public User register(User user, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] register: request={}", requestId,
				CommonUtils.convertToString(user));

		User response = new User();

		user.setCreatedDate(LocalDateTime.now());
		user.setStatus(Status.W);

		try {
			// Repository Call
			response = repository.save(user);
			String jwtToken = jwtService.generateToken(user);
			response.setAuthToken(jwtToken);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  register : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.REGISTRATION_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] register: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] authenticate: request={}", requestId,
				CommonUtils.convertToString(authRequest));

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

		// Repository Call
		var user = repository.findByEmail(authRequest.getEmail())
				.orElseThrow(() -> new TSMSException(TSMSError.USER_NOT_FOUND));
		var jwtToken = jwtService.generateToken(user);

		AuthenticationResponse response = AuthenticationResponse.builder().token(jwtToken).build();

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] authenticate: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public Optional<User> getByEmail(String email, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByEmail: request={}", requestId, email);

		Optional<User> response = repository.findByEmail(email);

		// Repository Call
		if (response.isEmpty()) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByEmail : ", requestId);
			throw new TSMSException(TSMSError.INVALID_EMAIL_OR_USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByEmail: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public User getByUserName(String userName, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByUserName: request={}", requestId, userName);

		User response = new User();

		try {
			// Repository Call
			response = repository.findByUserName(userName);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByUserName : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByUserName: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public boolean isEmailExists(String email, String requestId) {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] isEmailExists: request={}", requestId, email);

		// Repository Call
		boolean exists = repository.existsByEmail(email);

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] isEmailExists: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(exists));
		return exists;
	}

	@Override
	public boolean isUserNameExists(String userName, String requestId) {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] isUserNameExists: request={}", requestId, userName);

		// Repository Call
		boolean exists = repository.existsByUserName(userName);

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] isUserNameExists: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(exists));
		return exists;
	}
}
