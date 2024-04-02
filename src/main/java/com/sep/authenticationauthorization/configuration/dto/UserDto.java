package com.sep.authenticationauthorization.configuration.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.authenticationauthorization.configuration.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private String userName;

	private String nic;

	private String gender;

	private String salutation;

	private String dateOfBirth;

	private String contactNo;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String password;

	private String role;

	private Status status;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private String masterToken;

	private String authToken;

	private String profilePictureName;

	private String profilePictureContent;

	private String profilePictureUrl;

}
