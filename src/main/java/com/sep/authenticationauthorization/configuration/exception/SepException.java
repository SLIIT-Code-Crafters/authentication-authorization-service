package com.sep.authenticationauthorization.configuration.exception;

import lombok.Getter;

@Getter
public class SepException extends Exception {

	private static final long serialVersionUID = 1L;

	protected int errorCode;

	protected Exception e;

	public SepException(int errorCode) {
		this.errorCode = errorCode;
	}

	public SepException(String message) {
		super(message);
	}

	public SepException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public SepException(int errorCode, String message, Exception e) {
		super(message);
		this.errorCode = errorCode;
		this.e = e;
	}

}
