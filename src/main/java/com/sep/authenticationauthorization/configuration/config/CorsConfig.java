package com.sep.authenticationauthorization.configuration.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:4200", // Angular app running on localhost
				"http://travel-trek-angular-client.s3-website-ap-southeast-1.amazonaws.com" // Another example domain
		).allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH").allowedHeaders("*");
	}
}
