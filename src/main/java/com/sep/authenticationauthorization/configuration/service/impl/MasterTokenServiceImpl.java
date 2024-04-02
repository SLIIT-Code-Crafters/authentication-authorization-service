package com.sep.authenticationauthorization.configuration.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sep.authenticationauthorization.configuration.entity.MasterToken;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.repository.MasterTokenRepository;
import com.sep.authenticationauthorization.configuration.service.MasterTokenService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@Service
public class MasterTokenServiceImpl implements MasterTokenService{
	
	@Autowired
	private MasterTokenRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterTokenServiceImpl.class);

	@Override
	public MasterToken getMasterToken() throws TSMSException {
		
		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] getMasterToken: request={}");

		MasterToken response;

		try {
			// Repository Call
			response = repository.findAll().get(0);
		} catch (Exception e) {
			throw new TSMSException(TSMSError.MASTER_TOKEN_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] getMasterToken: timeTaken={}|response={}",
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

}
