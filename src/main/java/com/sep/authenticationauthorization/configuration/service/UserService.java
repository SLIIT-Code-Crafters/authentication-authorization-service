package com.sep.authenticationauthorization.configuration.service;

import com.sep.authenticationauthorization.configuration.entity.user.User;

public interface UserService {

	public User save(User user, String requestId);
	
	

}
