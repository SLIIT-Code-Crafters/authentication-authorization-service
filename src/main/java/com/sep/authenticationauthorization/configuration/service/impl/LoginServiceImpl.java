package com.sep.authenticationauthorization.configuration.service.impl;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;
import com.sep.authenticationauthorization.configuration.entity.User;
import com.sep.authenticationauthorization.configuration.enums.AccountStatus;
import com.sep.authenticationauthorization.configuration.enums.ApprovalStatus;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.service.LoginService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${adminApprovalStatusApi}")
	private String AdminApprovalStatusApiUrl;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Override
	public Boolean login(String userInput, String userNameOrEmail, String loginPassword, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] login: request={}", requestId, userNameOrEmail);

		String existingPassword = "";
		Boolean loginSuccess = Boolean.FALSE;
		Roles role;
		Long userId;
		String email;

		if (userInput.equals("EMAIL")) {

			// Service Call.
			Optional<User> user = authenticationService.getByEmail(userNameOrEmail, requestId);
			if (user.isEmpty()) {
				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}] login : Invalid Email or User Not Found", requestId);
				throw new TSMSException(TSMSError.INVALID_EMAIL_OR_USER_NOT_FOUND);
			} else {
				existingPassword = user.get().getPassword();
				role = user.get().getRole();
				userId = user.get().getId();
				email = user.get().getEmail();

				// Check if account is activated or not
				checkAccountStatus(userNameOrEmail, authenticationService, "EMAIL", requestId);
			}

		} else if (userInput.equals("USERNAME")) {

			// Service Call.
			User user = authenticationService.getByUserName(userNameOrEmail, requestId);
			if (user != null) {
				existingPassword = user.getPassword();
				role = user.getRole();
				userId = user.getId();
				email = user.getEmail();

				// Check if account is activated or not
				checkAccountStatus(userNameOrEmail, authenticationService, "USERNAME", requestId);

			} else {
				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}] login : Invalid Username or User Not Found",
						requestId);
				throw new TSMSException(TSMSError.INVALID_USERNAME_OR_USER_NOT_FOUND);
			}

		} else {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}] login : Invalid Email Address or Username", requestId);
			throw new TSMSException(TSMSError.INVALID_EMAIL_USERNAME);
		}

		// Check System Admin Approval
		if (role.equals(Roles.TO)) {
			String endpointUrl = AdminApprovalStatusApiUrl.concat(userId.toString()).concat("/").concat(email)
					.concat("?requestId=callFromAuthService");

			ResponseEntity<TSMSResponse> response = callAdminApprovalStatusApi(endpointUrl, requestId);

			@SuppressWarnings("unchecked")
			Map<String, Object> responseData = (Map<String, Object>) response.getBody().getData();

			if (responseData.get("approvalStatus").equals(ApprovalStatus.PENDING.name())) {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] login : Account Approval is in Pending Status",
						requestId);
				throw new TSMSException(TSMSError.ACCOUNT_APPROVAL_PENDING);
			} else if (responseData.get("approvalStatus").equals(ApprovalStatus.REJECTED.name())) {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] login : Account is Rejected by System Admin",
						requestId);
				throw new TSMSException(TSMSError.ACCOUNT_REJECTED);
			}
		}

		if (!passwordEncoder.matches(loginPassword, existingPassword)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] login : Password incorrect. Please verify your password and try again",
					requestId);
			throw new TSMSException(TSMSError.INCORRECT_PASSWORD);
		} else {
			loginSuccess = Boolean.TRUE;
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] login: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(loginSuccess));
		return loginSuccess;
	}

	public static void checkAccountStatus(String user, AuthenticationService service, String loginSource,
			String requestId) throws TSMSException {

		AccountStatus accountStatus = null;
		if (loginSource.equals("EMAIL")) {
			accountStatus = service.getAccountStatusByEmail(user, requestId);
		} else if (loginSource.equals("USERNAME")) {
			accountStatus = service.getAccountStatusByUserName(user, requestId);
		}

		if (accountStatus.equals(AccountStatus.PENDING)) {
			LOGGER.error(
					"ERROR [SERVICE-LAYER] [RequestId={}] login : Account is not Activated, Please activate your account before login",
					requestId);
			throw new TSMSException(TSMSError.ACCOUNT_NOT_ACTIVATED);
		} else if (accountStatus.equals(AccountStatus.INACTIVE) || accountStatus.equals(AccountStatus.DELETED)) {
			LOGGER.error(
					"ERROR [SERVICE-LAYER] [RequestId={}] login : Account is Inactive or Deactivated, Please create a new account",
					requestId);
			throw new TSMSException(TSMSError.ACCOUNT_IS_INACTIVE);
		}

	}

	private ResponseEntity<TSMSResponse> callAdminApprovalStatusApi(String endpointUrl, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] callAdminApprovalStatusApi: request={}", requestId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<TSMSResponse> response;

		try {
			response = restTemplate.getForEntity(endpointUrl, TSMSResponse.class);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  callAdminApprovalStatusApi : error={}|exception={}",
					requestId, TSMSError.APPROVAL_API_CALL_FAILED.getMessage(), e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_API_CALL_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] callAdminApprovalStatusApi: timeTaken={}|response={}",
				requestId, CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;

	}
}
