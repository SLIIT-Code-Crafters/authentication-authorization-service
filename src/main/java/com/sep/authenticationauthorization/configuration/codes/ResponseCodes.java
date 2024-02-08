package com.sep.authenticationauthorization.configuration.codes;

/*
Informational responses ( 100 – 199 )
Successful responses ( 200 – 299 )
Redirection messages ( 300 – 399 )
Client error responses ( 400 – 499 )
Server error responses ( 500 – 599 )
User response codes (1600 - 1649)
Location response codes (1650 - 1699)
*/
public enum ResponseCodes {

	OK(200), ALREADY_EXIST(1210), ALREADY_REPORTED(208), NOT_ACCEPTABLE(406), INVALID_REQUEST(400), NOT_FOUND(404),
	CREATED(201);

	private int code;

	private ResponseCodes(int code) {
		this.code = code;
	}

	public int code() {
		return code;
	}

}
