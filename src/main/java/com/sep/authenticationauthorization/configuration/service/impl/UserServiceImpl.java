package com.sep.authenticationauthorization.configuration.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.repository.UserRepository;
import com.sep.authenticationauthorization.configuration.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public User save(User user, String requestId) {

		User userResponse = new User();

		try {
			// Repository Call
			userResponse = repository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userResponse;

	}
}
