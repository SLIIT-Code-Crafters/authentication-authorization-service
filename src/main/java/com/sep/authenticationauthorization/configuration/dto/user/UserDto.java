package com.sep.authenticationauthorization.configuration.dto.user;

import java.util.Set;

import com.sep.authenticationauthorization.configuration.entity.role.Role;

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

	private String contactNo;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String userName;

	private String password;

	private Set<Role> role;

	private String masterToken;
}
