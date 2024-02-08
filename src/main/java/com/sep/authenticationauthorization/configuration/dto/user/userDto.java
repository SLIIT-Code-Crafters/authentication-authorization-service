package com.sep.authenticationauthorization.configuration.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class userDto {

	private Long id;

	private String firstName;

	private String lastName;

	private String userName;

	private String email;

	private String password;
}
