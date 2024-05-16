package com.sep.authenticationauthorization.configuration.dto.forgotpw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordRequest {

	private String email;

	private String otp;

	private String newPassword;

}
