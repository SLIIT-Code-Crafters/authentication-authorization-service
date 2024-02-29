package com.sep.authenticationauthorization.configuration.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL)
public class ResponseDto<T> {
	private String timestamp;

	private String requestId;

	private int statusCode;

	private String message;

	private T data;
	
	private String token;

	@Override
	public String toString() {
		return CommonUtils.convertToString(this);
	}

}
