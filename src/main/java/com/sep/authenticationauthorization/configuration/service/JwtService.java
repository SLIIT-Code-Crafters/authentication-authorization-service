package com.sep.authenticationauthorization.configuration.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	public String extractUserName (String token);
	
	public String generateToken(UserDetails userDetails);
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
	
	public boolean isTokenValid(String token, UserDetails userDetails);
}
