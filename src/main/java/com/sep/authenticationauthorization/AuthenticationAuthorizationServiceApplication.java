package com.sep.authenticationauthorization;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AuthenticationAuthorizationServiceApplication {

	@Value("${tsms.default.timeZone}")
	public String timeZone;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAuthorizationServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}

}
