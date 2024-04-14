package com.sep.authenticationauthorization.configuration.dto.activation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountActivationRequest {

	private String email;

	private String activationCode;

}
