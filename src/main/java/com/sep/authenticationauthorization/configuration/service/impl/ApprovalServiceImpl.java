package com.sep.authenticationauthorization.configuration.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sep.authenticationauthorization.configuration.entity.approval.Approval;
import com.sep.authenticationauthorization.configuration.enums.ApprovalStatus;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.repository.ApprovalRepository;
import com.sep.authenticationauthorization.configuration.service.ApprovalService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@Service
public class ApprovalServiceImpl implements ApprovalService{
	
	@Autowired
	private ApprovalRepository repository;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalServiceImpl.class);

	@Override
	public Approval save(Approval approval, String requestId) throws TSMSException {
		
		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] save: request={}", requestId,
				CommonUtils.convertToString(approval));

		Approval response = new Approval();

		approval.setCreatedDate(LocalDateTime.now());
		approval.setApprovalStatus(ApprovalStatus.PENDING);

		try {
			// Repository Call
			response = repository.save(approval);
		} catch (Exception e) {

			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  save : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] save: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

}
