package com.sep.authenticationauthorization.configuration.service;

import java.util.Optional;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;

public interface AuthenticationService {
	public User register(User user, String requestId) throws TSMSException;
	
	public Optional<User> getByEmail(String email, String requestId) throws TSMSException ;
	
	public boolean isEmailExists(String email, String requestId);
	
	public User getByUserName(String userName, String requestId) throws TSMSException ;
	
	public boolean isUserNameExists(String userName, String requestId);
	
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId) throws TSMSException;
}
