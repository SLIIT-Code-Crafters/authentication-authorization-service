package com.sep.authenticationauthorization.configuration.service;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.entity.user.User;

public interface AuthenticationService {
	public User register(User user, String requestId);
	
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId);
}
