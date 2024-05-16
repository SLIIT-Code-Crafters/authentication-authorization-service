package com.sep.authenticationauthorization.configuration.service;

import java.util.Optional;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.dto.forgotpw.ForgotPasswordRequest;
import com.sep.authenticationauthorization.configuration.entity.User;
import com.sep.authenticationauthorization.configuration.enums.AccountStatus;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;

public interface AuthenticationService {
	public User register(User user, String requestId) throws TSMSException;

	public Optional<User> getByEmail(String email, String requestId) throws TSMSException;

	public boolean isEmailExists(String email, String requestId);

	public User getByUserName(String userName, String requestId) throws TSMSException;

	public boolean isUserNameExists(String userName, String requestId);

	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId)
			throws TSMSException;

	public AccountStatus getAccountStatusByUserName(String userName, String requestId) throws TSMSException;

	public AccountStatus getAccountStatusByEmail(String email, String requestId) throws TSMSException;

	public Boolean activateUserAccount(String email, String activationCode, String requestId) throws TSMSException;

	public Boolean verifyUserAccount(String email, String requestId) throws TSMSException;

	public Boolean forgotPassword(ForgotPasswordRequest forgotPwdRequest, String requestId) throws TSMSException;
}
