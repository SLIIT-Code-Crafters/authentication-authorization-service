package com.sep.authenticationauthorization.configuration.codes;

public enum TSMSError {

	OK("001", 200, "OK"), 
	ALREADY_EXIST("002", 409, "Already Exists"), 
	INVALID_REQUEST("003", 400, "Invalid Request"),
	INVALID_ROLE("004", 400, "Invalid Role"), 
	INVALID_CONTACT_NO("005", 400, "Invalid Contact Number"),
	NOT_FOUND("006", 404, "Not Found"), 
	CREATED("007", 201, "Created"),
	MANDOTORY_FIELDS_EMPTY("008", 400, "Mandatory fields are null. Please ensure all required fields are provided"),
	INVALID_SALUTATION("009", 400, "Invalid Salutation"), 
	INVALID_FIRST_NAME("010", 400, "Invalid First Name"),
	INVALID_LAST_NAME("011", 400, "Invalid Last Name"), 
	INVALID_PASSWORD("012", 400, "Invalid Password"),
	USER_NOT_FOUND("013", 404, "User Not Found"),
	REGISTRATION_FAILED("014", 404, "Registration Failed"),
	INCORRECT_PASSWORD("015", 401, "Password incorrect. Please verify your password and try again"),
	MASTER_TOKEN_NOT_FOUND("016", 404, "Master Token Not Found"),
	MASTER_TOKEN_MANDATORY("017", 404, "Master Token mandatory for System Admin"),
	INVALID_MASTER_TOKEN("018", 400, "Invalid Master Token"),
	AUTH_TOKEN_EXPIRED("019", 401, "Authentication Token Expired"),
	INVALID_EMAIL("020", 400, "Invalid Email Address"),
	INVALID_USERNAME("022", 400, "Invalid UserName"),
	EMAIL_EXIST("023", 409, "An account associated with this email already exists"),
	USERNAME_EXIST("024", 409, "An account associated with this username already exists");

	private int status;
	private String code;
	private String message;

	private TSMSError(String errorCode, int errorStatus, String errorMessage) {
		this.status = errorStatus;
		this.code = errorCode;
		this.message = errorMessage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String errorCode) {
		this.code = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String errorMessage) {
		this.message = errorMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
