package com.sep.authenticationauthorization.configuration.exception;

import com.sep.authenticationauthorization.configuration.codes.TSMSError;

public class TSMSException extends Exception {

	private TSMSError error;

	public TSMSException(TSMSError exceptionError) {
		this.error = exceptionError;
	}

	public TSMSError getError() {
		return error;
	}

	public void setError(TSMSError error) {
		this.error = error;
	}
}
