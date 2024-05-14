package com.sep.authenticationauthorization.configuration.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;
import com.sep.authenticationauthorization.configuration.entity.Approval;
import com.sep.authenticationauthorization.configuration.entity.User;
import com.sep.authenticationauthorization.configuration.enums.AccountStatus;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.enums.Status;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.repository.UserRepository;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.service.JwtService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${approvalRequestApi}")
	private String ApprovalRequestApiUrl;

	@Value("${accountActivationEmailSendApi}")
	private String AccountActivationEmailSendApiUrl;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Override
	@Transactional(rollbackFor = Exception.class)
	public User register(User user, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] register: request={}", requestId,
				CommonUtils.convertToString(user));

		User response = new User();

		user.setCreatedDate(LocalDateTime.now());
		user.setStatus(Status.W);
		user.setAccountStatus(AccountStatus.PENDING);
		user.setActivationCode(CommonUtils.generateActivationCode());

		try {
			// Repository Call
			response = repository.save(user);
			String jwtToken = jwtService.generateToken(user);
			response.setAuthToken(jwtToken);

			if (user.getRole().equals(Roles.TO)) {
				Approval approval = new Approval();
				approval.setId(response.getId());
				approval.setContent(response.getFirstName().concat(" ").concat(response.getLastName())
						.concat(" wants to create an Trip Organizer account"));
				approval.setCreatedBy(response.getFirstName().concat(" ").concat(response.getLastName()));
				approval.setEmail(response.getEmail());
				approval.setReason("Trip Organizer Account Creation Request");

				// Call Trip-Management-Service Save Approval POST Api.
				ResponseEntity<TSMSResponse> approveApiResponse = callApprovalRequestApi(approval, requestId);
				if (approveApiResponse.getBody().getStatus() != 200) {
					LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  register : error={}", requestId,
							TSMSError.APPROVAL_REQUEST_CREATION_FAILED.getMessage());
					throw new TSMSException(TSMSError.APPROVAL_REQUEST_CREATION_FAILED);
				}
			}

			// Call Send user activation email.
			String recipientName = response.getFirstName().concat(" ").concat(response.getLastName());
			String recipientEmail = response.getEmail();
			String activationCode = response.getActivationCode();
			ResponseEntity<TSMSResponse> emailSendApiResponse = callAccountActivationEmailSendRequestApi(recipientName,
					recipientEmail, activationCode, requestId);
			if (emailSendApiResponse.getBody().getStatus() != 200) {
				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  register : error={}", requestId,
						TSMSError.ACCOUNT_ACTIVATION_EMAIL_SEND_FAILED.getMessage());
				throw new TSMSException(TSMSError.ACCOUNT_ACTIVATION_EMAIL_SEND_FAILED);
			}

		} catch (Exception e) {

			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  register : exception={}", requestId, e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.REGISTRATION_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] register: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest authRequest, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] authenticate: request={}", requestId,
				CommonUtils.convertToString(authRequest));

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

		// Repository Call
		var user = repository.findByEmail(authRequest.getEmail())
				.orElseThrow(() -> new TSMSException(TSMSError.USER_NOT_FOUND));
		var jwtToken = jwtService.generateToken(user);

		AuthenticationResponse response = AuthenticationResponse.builder().token(jwtToken).build();

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] authenticate: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public Optional<User> getByEmail(String email, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByEmail: request={}", requestId, email);

		Optional<User> response = repository.findByEmail(email);

		// Repository Call
		if (response.isEmpty()) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByEmail : ", requestId);
			throw new TSMSException(TSMSError.INVALID_EMAIL_OR_USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByEmail: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public User getByUserName(String userName, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getByUserName: request={}", requestId, userName);

		User response = new User();

		try {
			// Repository Call
			response = repository.findByUserName(userName);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getByUserName : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getByUserName: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;
	}

	@Override
	public boolean isEmailExists(String email, String requestId) {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] isEmailExists: request={}", requestId, email);

		// Repository Call
		boolean exists = repository.existsByEmail(email);

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] isEmailExists: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(exists));
		return exists;
	}

	@Override
	public boolean isUserNameExists(String userName, String requestId) {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] isUserNameExists: request={}", requestId, userName);

		// Repository Call
		boolean exists = repository.existsByUserName(userName);

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] isUserNameExists: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(exists));
		return exists;
	}

	@Override
	public AccountStatus getAccountStatusByUserName(String userName, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getAccountStatusByUserName: request={}", requestId, userName);

		User response = new User();

		try {
			// Repository Call
			response = repository.findByUserName(userName);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getAccountStatusByUserName : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getAccountStatusByUserName: timeTaken={}|response={}",
				requestId, CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response.getAccountStatus() != null ? response.getAccountStatus() : null;
	}

	@Override
	public AccountStatus getAccountStatusByEmail(String email, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] getAccountStatusByEmail: request={}", requestId, email);

		Optional<User> response;

		try {
			// Repository Call
			response = repository.findByEmail(email);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  getAccountStatusByEmail : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] getAccountStatusByEmail: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response.isPresent() ? response.get().getAccountStatus() : null;
	}

	@Override
	public Boolean activateUserAccount(String email, String activationCode, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] activateUserAccount: email={}|activationCode={}", requestId,
				email, activationCode);

		Optional<User> response;
		Boolean result = Boolean.FALSE;
		try {
			response = repository.findByEmail(email);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  activateUserAccount : exception={}", requestId,
					e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		if (response.isPresent()) {

			if (response.get().getActivationCode().equals(activationCode)) {
				response.get().setAccountStatus(AccountStatus.ACTIVE);
				response.get().setUpdatedDate(LocalDateTime.now());
				try {
					repository.save(response.get());
				} catch (Exception e) {
					LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  activateUserAccount :", requestId);
					e.printStackTrace();
					throw new TSMSException(TSMSError.ACCOUNT_ACTIVATION_FAILED);
				}
			} else {
				LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  activateUserAccount : error={} ", requestId,
						TSMSError.INVALID_ACTIVATION_CODE.getMessage());
				throw new TSMSException(TSMSError.INVALID_ACTIVATION_CODE);
			}

		} else {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  activateUserAccount : error={} ", requestId,
					TSMSError.USER_NOT_FOUND.getMessage());
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		}

		result = Boolean.TRUE;

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] activateUserAccount: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return result;
	}

	private ResponseEntity<TSMSResponse> callApprovalRequestApi(Approval approval, String requestId)
			throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [SERVICE-LAYER] [RequestId={}] callApprovalRequestApi: request={}", requestId);

		String requestBodyJson = generateApprovalRequestBodyJson(approval.getId(), approval.getEmail(),
				approval.getContent(), approval.getReason(), approval.getCreatedBy());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

		ResponseEntity<TSMSResponse> response;

		try {
			response = restTemplate.postForEntity(ApprovalRequestApiUrl, requestEntity, TSMSResponse.class);
		} catch (Exception e) {
			LOGGER.error("ERROR [SERVICE-LAYER] [RequestId={}]  callApprovalRequestApi : error={}|exception={}",
					requestId, TSMSError.APPROVAL_API_CALL_FAILED.getMessage(), e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.APPROVAL_API_CALL_FAILED);
		}

		LOGGER.info("END [SERVICE-LAYER] [RequestId={}] callApprovalRequestApi: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;

	}

	private String generateApprovalRequestBodyJson(long id, String email, String content, String reason,
			String createdBy) {
		return String.format("{\"id\":%d,\"email\":\"%s\",\"content\":\"%s\",\"reason\":\"%s\",\"createdBy\":\"%s\"}",
				id, email, content, reason, createdBy);
	}

	private ResponseEntity<TSMSResponse> callAccountActivationEmailSendRequestApi(String recipientName,
			String recipientEmail, String activationCode, String requestId) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info(
				"START [SERVICE-LAYER] [RequestId={}] callAccountActivationEmailSendRequestApi: recipientName={}|recipientEmail={}|activationCode={}",
				requestId, recipientName, recipientEmail, activationCode);

		String requestBodyJson = generateAccountActivationEmailSendRequestBodyJson(recipientName, recipientEmail,
				activationCode);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

		ResponseEntity<TSMSResponse> response;

		try {
			response = restTemplate.postForEntity(AccountActivationEmailSendApiUrl, requestEntity, TSMSResponse.class);
		} catch (Exception e) {
			LOGGER.error(
					"ERROR [SERVICE-LAYER] [RequestId={}]  callAccountActivationEmailSendRequestApi : error={}|exception={}",
					requestId, TSMSError.ACCOUNT_ACTIVATION_EMAIL_SEND_API_CALL_FAILED.getMessage(), e.getMessage());
			e.printStackTrace();
			throw new TSMSException(TSMSError.ACCOUNT_ACTIVATION_EMAIL_SEND_API_CALL_FAILED);
		}

		LOGGER.info(
				"END [SERVICE-LAYER] [RequestId={}] callAccountActivationEmailSendRequestApi: timeTaken={}|response={}",
				requestId, CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return response;

	}

	private String generateAccountActivationEmailSendRequestBodyJson(String recipientName, String recipientEmail,
			String activationCode) {
		return String.format("{\"recipientName\":\"%s\",\"recipientEmail\":\"%s\",\"activationCode\":\"%s\"}",
				recipientName, recipientEmail, activationCode);
	}

}
