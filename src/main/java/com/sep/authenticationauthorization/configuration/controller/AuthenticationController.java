package com.sep.authenticationauthorization.configuration.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.authenticationauthorization.configuration.dto.UserDto;
import com.sep.authenticationauthorization.configuration.dto.activation.AccountActivationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationResponse;
import com.sep.authenticationauthorization.configuration.dto.response.TSMSResponse;
import com.sep.authenticationauthorization.configuration.entity.MasterToken;
import com.sep.authenticationauthorization.configuration.entity.User;
import com.sep.authenticationauthorization.configuration.enums.AccountStatus;
import com.sep.authenticationauthorization.configuration.enums.Gender;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.enums.Salutation;
import com.sep.authenticationauthorization.configuration.exception.TSMSError;
import com.sep.authenticationauthorization.configuration.exception.TSMSException;
import com.sep.authenticationauthorization.configuration.service.AuthenticationService;
import com.sep.authenticationauthorization.configuration.service.MasterTokenService;
import com.sep.authenticationauthorization.configuration.utill.CommonUtils;

@RestController
@RequestMapping("/api/v1/private/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService service;

	@Autowired
	private MasterTokenService masterTokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@PostMapping("/register")
	public ResponseEntity<TSMSResponse> register(@RequestParam("requestId") String requestId,
			@RequestBody UserDto userDto) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] register: request={}", requestId,
				CommonUtils.convertToString(userDto));

		TSMSResponse response = new TSMSResponse();

		if (!CommonUtils.checkMandtoryFieldsNullOrEmpty(userDto)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] register : Mandatory fields are null. Please ensure all required fields are provided",
					requestId);
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		if (!CommonUtils.containsOnlyLetters(userDto.getFirstName())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid First Name", requestId);
			throw new TSMSException(TSMSError.INVALID_FIRST_NAME);
		}

		if (!CommonUtils.containsOnlyLetters(userDto.getLastName())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Last Name", requestId);
			throw new TSMSException(TSMSError.INVALID_LAST_NAME);
		}

		if (!CommonUtils.isValidEmail(userDto.getEmail())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Email Address", requestId);
			throw new TSMSException(TSMSError.INVALID_EMAIL);
		}

		if (!CommonUtils.isValidUserName(userDto.getUserName())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid UserName", requestId);
			throw new TSMSException(TSMSError.INVALID_USERNAME);
		}

		if (service.isEmailExists(userDto.getEmail(), requestId)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] register : An account associated with this email already exists",
					requestId);
			throw new TSMSException(TSMSError.EMAIL_EXIST);
		}

		if (service.isUserNameExists(userDto.getUserName(), requestId)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] register : An account associated with this username already exists",
					requestId);
			throw new TSMSException(TSMSError.USERNAME_EXIST);
		}

		if (!CommonUtils.validatePhoneNumber(userDto.getContactNo())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Contact Number", requestId);
			throw new TSMSException(TSMSError.INVALID_CONTACT_NO);
		}

		if (!CommonUtils.isValidateRole(userDto.getRole())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Role", requestId);
			throw new TSMSException(TSMSError.INVALID_ROLE);
		}

		if (!CommonUtils.isValidateSalutation(userDto.getSalutation())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Salutation", requestId);
			throw new TSMSException(TSMSError.INVALID_SALUTATION);
		}

		if (!CommonUtils.isValidPassword(userDto.getPassword())) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Password", requestId);
			throw new TSMSException(TSMSError.INVALID_PASSWORD);
		}

		if (userDto.getRole().equals(Roles.SA.name())) {
			if (!CommonUtils.checkMasterTokenNullOrEmpty(userDto.getMasterToken())) {
				LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Master Token mandatory for System Admin",
						requestId);
				throw new TSMSException(TSMSError.MASTER_TOKEN_MANDATORY);
			} else {
				MasterToken masterToken = masterTokenService.getMasterToken();
				if (!userDto.getMasterToken().equals(masterToken.getMasterToken())) {
					LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : Invalid Master Token", requestId);
					throw new TSMSException(TSMSError.INVALID_MASTER_TOKEN);
				}
			}

		}

		// Service Call.
		UserDto dto = convertEntityToDto(service.register(convertDtoToEntity(userDto), requestId));
		response.setRequestId(requestId);
		response.setSuccess(true);
		response.setData(dto);
		response.setToken(dto.getAuthToken());
		response.setMessage("User Registered Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] register: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<TSMSResponse> authenticate(@RequestParam("requestId") String requestId,
			@RequestBody AuthenticationRequest authRequest) throws TSMSException {

		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] authenticate: request={}", requestId,
				CommonUtils.convertToString(authRequest));

		TSMSResponse response = new TSMSResponse();
		response.setRequestId(requestId);

		if (!CommonUtils.checkAuthMandtoryFieldsNullOrEmpty(authRequest)) {
			LOGGER.error(
					"ERROR [REST-LAYER] [RequestId={}] register : Mandatory fields are null. Please ensure all required fields are provided",
					requestId);
			throw new TSMSException(TSMSError.MANDOTORY_FIELDS_EMPTY);
		}

		Optional<User> user = service.getByEmail(authRequest.getEmail(), requestId);
		AccountStatus accountStatus;
		if (user.isEmpty()) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] register : User Not Found", requestId);
			throw new TSMSException(TSMSError.USER_NOT_FOUND);
		} else {
			if (!passwordEncoder.matches(authRequest.getPassword(), user.get().getPassword())) {
				LOGGER.error(
						"ERROR [REST-LAYER] [RequestId={}] register : Password incorrect. Please verify your password and try again",
						requestId);
				throw new TSMSException(TSMSError.INCORRECT_PASSWORD);
			} else {

				accountStatus = user.get().getAccountStatus();

				if (accountStatus.equals(AccountStatus.PENDING)) {
					LOGGER.error(
							"ERROR [REST-LAYER] [RequestId={}] login : Account is not Activated, Please activate your account before login",
							requestId);
					throw new TSMSException(TSMSError.ACCOUNT_NOT_ACTIVATED);
				} else if (accountStatus.equals(AccountStatus.INACTIVE)
						|| accountStatus.equals(AccountStatus.DELETED)) {
					LOGGER.error(
							"ERROR [REST-LAYER] [RequestId={}] login : Account is Inactive or Deactivated, Please create a new account",
							requestId);
					throw new TSMSException(TSMSError.ACCOUNT_IS_INACTIVE);
				}
			}
		}

		// Service Call.
		AuthenticationResponse authResponse = service.authenticate(authRequest, requestId);
		response.setSuccess(true);
		response.setData(authResponse);
		response.setMessage("Authenticate Successfully");
		response.setStatus(TSMSError.OK.getStatus());
		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] authenticate: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));
		return ResponseEntity.ok(response);
	}

	@PutMapping("/activate")
	public ResponseEntity<TSMSResponse> activateUserAccount(@RequestParam("requestId") String requestId,
			@RequestBody AccountActivationRequest accountActivateRequest) throws TSMSException {

		TSMSResponse response = new TSMSResponse();
		long startTime = System.currentTimeMillis();
		LOGGER.info("START [REST-LAYER] [RequestId={}] activateUserAccount: request={}", requestId,
				accountActivateRequest.getEmail());

		if (accountActivateRequest.getEmail() == null || accountActivateRequest.getEmail().equals("")
				|| accountActivateRequest.getEmail().isEmpty()) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] activateUserAccount : Email Filed is Mandatory", requestId);
			throw new TSMSException(TSMSError.EMAIL_FIELD_EMPTY);
		}

		if (accountActivateRequest.getActivationCode() == null || accountActivateRequest.getActivationCode().equals("")
				|| accountActivateRequest.getActivationCode().isEmpty()) {
			LOGGER.error("ERROR [REST-LAYER] [RequestId={}] activateUserAccount : Activation Code is Mandatory",
					requestId);
			throw new TSMSException(TSMSError.ACTIVATION_CODE_EMPTY);
		}

		// Service Call.
		Boolean result = service.activateUserAccount(accountActivateRequest.getEmail(),
				accountActivateRequest.getActivationCode(), requestId);

		if (result) {
			response.setSuccess(true);
			response.setRequestId(requestId);
			response.setMessage("Account Activated Successfully");
			response.setStatus(TSMSError.OK.getStatus());
		} else {
			response.setSuccess(true);
			response.setRequestId(requestId);
			response.setMessage("Account Activation Failed");
			response.setStatus(TSMSError.ACCOUNT_ACTIVATION_FAILED.getStatus());
		}

		response.setTimestamp(LocalDateTime.now().toString());

		LOGGER.info("END [REST-LAYER] [RequestId={}] activateUserAccount: timeTaken={}|response={}", requestId,
				CommonUtils.getExecutionTime(startTime), CommonUtils.convertToString(response));

		return ResponseEntity.ok(response);
	}

	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setUserName(user.getOriginalUsername());
		userDto.setNic(user.getNic());
		if (user.getGender() != null) {
			userDto.setGender(user.getGender().name());
		}

		if (user.getSalutation() != null) {
			userDto.setSalutation(user.getSalutation().name());
		}
		userDto.setDateOfBirth(user.getDateOfBirth());
		userDto.setContactNo(user.getContactNo());
		userDto.setAddressLine1(user.getAddressLine1());
		userDto.setAddressLine2(user.getAddressLine2());
		userDto.setAddressLine3(user.getAddressLine3());
		userDto.setPassword(passwordEncoder.encode(user.getPassword()));
		userDto.setRole(user.getRole().name());
		userDto.setMasterToken(user.getMasterToken());
		userDto.setAuthToken(user.getAuthToken());

		return userDto;
	}

	private User convertDtoToEntity(UserDto userDto) {
		User user = new User();

		String role = userDto.getRole();

		if (role.equals(Roles.SA.name())) {
			user.setRole(Roles.SA);
		} else if (role.equals(Roles.TO.name())) {
			user.setRole(Roles.TO);
		} else if (role.equals(Roles.TP.name())) {
			user.setRole(Roles.TP);
		}

		String salutation = userDto.getSalutation();

		if (salutation.equals(Salutation.DR.name())) {
			user.setSalutation(Salutation.DR);
		} else if (salutation.equals(Salutation.HON.name())) {
			user.setSalutation(Salutation.HON);
		} else if (salutation.equals(Salutation.MISS.name())) {
			user.setSalutation(Salutation.MISS);
		} else if (salutation.equals(Salutation.MR.name())) {
			user.setSalutation(Salutation.MR);
		} else if (salutation.equals(Salutation.MRS.name())) {
			user.setSalutation(Salutation.MRS);
		} else if (salutation.equals(Salutation.MS.name())) {
			user.setSalutation(Salutation.MS);
		} else if (salutation.equals(Salutation.REV.name())) {
			user.setSalutation(Salutation.REV);
		}

		if (userDto.getGender() != null) {

			String gender = userDto.getGender();

			if (gender.equals(Gender.M.name())) {
				user.setGender(Gender.M);
			} else if (gender.equals(Gender.F.name())) {
				user.setGender(Gender.F);
			} else if (gender.equals(Gender.O.name())) {
				user.setGender(Gender.O);
			}
		}

		user.setId(userDto.getId());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setUserName(userDto.getUserName());
		user.setNic(userDto.getNic());

		user.setDateOfBirth(userDto.getDateOfBirth());
		user.setContactNo(userDto.getContactNo());
		user.setAddressLine1(userDto.getAddressLine1());
		user.setAddressLine2(userDto.getAddressLine2());
		user.setAddressLine3(userDto.getAddressLine3());

		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setMasterToken(userDto.getMasterToken());

		return user;
	}
}
