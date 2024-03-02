package com.sep.authenticationauthorization.configuration.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sep.authenticationauthorization.configuration.enums.Gender;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.enums.Salutation;
import com.sep.authenticationauthorization.configuration.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private String userName;

	private String nic;

	private Gender gender;

	private Salutation salutation;

	private LocalDate dateOfBirth;

	private String contactNo;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String password;

//	private Set<Role> role;

	private Roles role;

	private Status status;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private String masterToken;

	private String authToken;
}
