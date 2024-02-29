package com.sep.authenticationauthorization.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sep.authenticationauthorization.configuration.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User save(User user);

	Optional<User> findByEmail(String email);
}
