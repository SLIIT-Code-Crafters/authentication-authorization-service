package com.sep.authenticationauthorization.configuration.service;

import com.sep.authenticationauthorization.configuration.entity.MasterToken;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;

public interface MasterTokenService {
	
	MasterToken getMasterToken () throws TSMSException ;

}
