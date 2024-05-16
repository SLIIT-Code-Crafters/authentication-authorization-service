package com.sep.authenticationauthorization.configuration.exception;

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
	INVALID_USERNAME("021", 400, "Invalid UserName"),
	EMAIL_EXIST("022", 409, "An account associated with this email already exists"),
	USERNAME_EXIST("023", 409, "An account associated with this username already exists"),
	INVALID_EMAIL_USERNAME("024", 400, "Invalid Email Address or Username"),
	INVALID_EMAIL_OR_USER_NOT_FOUND("025", 404, "Invalid Email or User Not Found"),
	INVALID_USERNAME_OR_USER_NOT_FOUND("026", 404, "Invalid Username or User Not Found"),
	APPROVAL_REQUEST_CREATION_FAILED("027", 404, "Approval Request Creation Failed"),
	APPROVAL_API_CALL_FAILED("028", 404, "Approval Api Call Failed"),
	ACCOUNT_NOT_ACTIVATED("029", 403, "Account is not Activated, Please activate your account before login"),
	ACCOUNT_IS_INACTIVE("030", 203, "Account is Inactive or Deactivated, Please create a new account"),
	FAILED("031", 401, "FAILED"),
	ACCOUNT_APPROVAL_PENDING("032", 202, "Account Approval is in Pending Status"),
	ACCOUNT_REJECTED("033", 403, "Account is Rejected by System Admin"),
	ACCOUNT_ACTIVATION_EMAIL_SEND_API_CALL_FAILED("034", 404, "Account Activation Email Sending Api Call Failed"),
	ACCOUNT_ACTIVATION_EMAIL_SEND_FAILED("035", 404, "Account Activation Email Sending Failed"),
	ACCOUNT_ACTIVATION_FAILED("036", 404, "Account Activation Failed"),
	EMAIL_FIELD_EMPTY("037", 404, "Email Field is Empty"),
	INVALID_ACTIVATION_CODE("038", 400, "Incrorrect Activation Code. Please verify your activation code and try again"),
	ACTIVATION_CODE_EMPTY("039", 404, "Activation Code is Empty"),
	ACCOUNT_VERIFICATION_FAILED("040", 404, "Account Verification Failed"),
	PASSWORD_RESET_FAILED("041", 404, "Password Reset Failed"),
	OTP_EMPTY("042", 404, "OTP is Empty"),
	PASSWORD_FIELD_EMPTY("043", 404, "Password Field is Empty"),
	OTP_NOT_FOUND("044", 404, "OTP Not Found"),
	INVALID_OTP("045", 400, "Incrorrect OTP. Please verify your OTP and try again"),
	EMAIL_SEND_API_CALL_FAILED("046", 404, "Email Sending Api Call Failed"),
	WELCOME_EMAIL_SEND_API_CALL_FAILED("047", 404, "Welcome Email Sending Api Call Failed"),
	OTP_EMAIL_SEND_API_CALL_FAILED("048", 404, "OTP Email Sending Api Call Failed"),
	PWD_RESET_SUCCESS_EMAIL_SEND_API_CALL_FAILED("049", 404, "Password Reset Success Email Sending Api Call Failed");
	
	

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
