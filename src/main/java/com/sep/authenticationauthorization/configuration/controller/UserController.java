package com.sep.authenticationauthorization.configuration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/private/users")
public class UserController {
	
	@GetMapping("/login")
	public ResponseEntity<String> login() {

		return ResponseEntity.ok("Successfully Logged In!");
	}

}
