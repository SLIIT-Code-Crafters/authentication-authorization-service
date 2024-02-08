package com.sep.authenticationauthorization.configuration.repository;

import org.springframework.data.repository.Repository;

import com.sep.authenticationauthorization.configuration.entity.user.User;

public interface UserRepository extends Repository<User, Long> {
	public User save(User user);
}
