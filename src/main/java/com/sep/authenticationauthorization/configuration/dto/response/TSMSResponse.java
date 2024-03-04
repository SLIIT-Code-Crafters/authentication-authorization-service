package com.sep.authenticationauthorization.configuration.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TSMSResponse {
	private String timestamp;

	private String requestId;

	private boolean success;

	private String message;

	private int status;

	private String code;

	private Object data;

	private String token;

	@Override
	public String toString() {
		return CommonUtils.convertToString(this);
	}

}
