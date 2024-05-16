package com.sep.authenticationauthorization.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.authenticationauthorization.configuration.entity.PasswordRecoveryQueue;
import com.sep.authenticationauthorization.configuration.enums.PasswordRecoveryOTPStatus;

@Repository
public interface PasswordRecoveryQueueRepository extends JpaRepository<PasswordRecoveryQueue, Long> {

	Optional<PasswordRecoveryQueue> findByEmailAndStatus(String email, PasswordRecoveryOTPStatus status);

}
