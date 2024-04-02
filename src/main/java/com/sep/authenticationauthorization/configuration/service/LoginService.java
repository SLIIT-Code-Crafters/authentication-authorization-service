package com.sep.authenticationauthorization.configuration.service;

import com.sep.authenticationauthorization.configuration.exception.TSMSException;

public interface LoginService {

	public Boolean login(String userInput, String userNameOrEmail, String password, String requestId)
			throws TSMSException;

}
