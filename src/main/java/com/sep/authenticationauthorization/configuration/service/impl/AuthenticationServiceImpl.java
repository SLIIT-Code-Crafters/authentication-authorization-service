package com.sep.authenticationauthorization.configuration.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.enums.Status;
import com.sep.authenticationauthorization.configuration.repository.UserRepository;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.service.JwtService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public User register(User user, String requestId) {

		User userResponse = new User();

		user.setCreatedDate(LocalDateTime.now());
		user.setStatus(Status.WHITELISTED);

		try {
			// Repository Call
			userResponse = repository.save(user);
			String jwtToken = jwtService.generateToken(user);
			userResponse.setAuthToken(jwtToken);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userResponse;
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

		var user = repository.findByEmail(authRequest.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
		var jwtToken = jwtService.generateToken(user);

		return AuthenticationResponse.builder().token(jwtToken).build();
	}
}
