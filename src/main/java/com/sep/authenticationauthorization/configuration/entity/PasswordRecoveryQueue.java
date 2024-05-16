package com.sep.authenticationauthorization.configuration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.authenticationauthorization.configuration.enums.PasswordRecoveryOTPStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Password_Recovery_Queue")
public class PasswordRecoveryQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "otp", nullable = false)
	private String otp;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private PasswordRecoveryOTPStatus status;

}
