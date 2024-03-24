package com.sep.authenticationauthorization.configuration.service;

import com.sep.authenticationauthorization.configuration.entity.approval.Approval;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;

public interface ApprovalService {
	Approval save (Approval approval, String requestId) throws TSMSException;
}
