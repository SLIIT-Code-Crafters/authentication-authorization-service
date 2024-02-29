package com.sep.authenticationauthorization.configuration.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sep.authenticationauthorization.configuration.codes.ResponseCodes;
import com.sep.authenticationauthorization.configuration.dto.response.ResponseDto;
import com.sep.authenticationauthorization.configuration.dto.user.userDto;
import com.sep.authenticationauthorization.configuration.entity.user.User;
import com.sep.authenticationauthorization.configuration.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/private/users")
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<ResponseDto<userDto>> registerUser(@RequestParam("requestId") String requestId,
			@RequestBody userDto userDto) {

		ResponseDto<userDto> response = new ResponseDto<>();
		response.setRequestId(requestId);

		// Service Call Test.
		userDto dto = convertEntityToDto(service.save(convertDtoToEntity(userDto), requestId));
		response.setData(dto);
		response.setMessage("User Saved Successfully");
		response.setStatusCode(ResponseCodes.OK.code());
		response.setTimestamp(LocalDateTime.now().toString());

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<ResponseDto<userDto>> authenticateUser(@RequestParam("requestId") String requestId,
			@RequestBody userDto userDto) {

		ResponseDto<userDto> response = new ResponseDto<>();
//		response.setRequestId(requestId);
//
//		// Service Call Test.
//		userDto dto = convertEntityToDto(service.save(convertDtoToEntity(userDto), requestId));
//		response.setData(dto);
//		response.setMessage("User Saved Successfully");
//		response.setStatusCode(ResponseCodes.OK.code());
//		response.setTimestamp(LocalDateTime.now().toString());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-user")
	public ResponseEntity<String> getUser() {

		return ResponseEntity.ok("Deployment Success Update V2!");
	}

	private userDto convertEntityToDto(User user) {
		userDto userDto = new userDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setContactNo(user.getContactNo());
		userDto.setAddressLine1(user.getAddressLine1());
		userDto.setAddressLine2(user.getAddressLine2());
		userDto.setAddressLine3(user.getAddressLine3());
		userDto.setUserName(user.getOriginalUsername());
		userDto.setPassword(passwordEncoder.encode(user.getPassword()));
		userDto.setRole(user.getRole());
		userDto.setMasterToken(user.getMasterToken());

		return userDto;
	}

	private User convertDtoToEntity(userDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setContactNo(userDto.getContactNo());
		user.setAddressLine1(userDto.getAddressLine1());
		user.setAddressLine2(userDto.getAddressLine2());
		user.setAddressLine3(userDto.getAddressLine3());
		user.setUserName(userDto.getUserName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRole(userDto.getRole());
		user.setMasterToken(userDto.getMasterToken());

		return user;
	}

}
