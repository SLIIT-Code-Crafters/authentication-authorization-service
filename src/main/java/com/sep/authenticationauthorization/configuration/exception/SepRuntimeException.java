package com.sep.authenticationauthorization.configuration.exception;

import lombok.Getter;

@Getter
public class SepRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected int errorCode;

	public SepRuntimeException(int errorCode) {
		this.errorCode = errorCode;
	}

	public SepRuntimeException(String message) {
		super(message);
	}

	public SepRuntimeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
