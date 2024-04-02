package com.sep.authenticationauthorization.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.authenticationauthorization.configuration.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	User findByUserName(String username);

	boolean existsByEmail(String email);

	boolean existsByUserName(String userName);

}
