package com.sep.authenticationauthorization.configuration.vo;

import com.sep.authenticationauthorization.configuration.enums.EmailType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailRequestVo {

	String recipientName;

	String recipientEmail;

	String activationCode;

	String otp;

	EmailType emailType;

}
