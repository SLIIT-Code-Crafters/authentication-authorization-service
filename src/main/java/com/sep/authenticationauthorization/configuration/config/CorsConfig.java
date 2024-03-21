package com.sep.authenticationauthorization.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration	
public class CorsConfig  {
	
	@Bean
	public WebMvcConfigurer corsConfigure() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:4200","http://travel-trek-angular-client.s3-website-ap-southeast-1.amazonaws.com")
						.allowedMethods(HttpMethod.GET.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name(),
						HttpMethod.PATCH.name())
						.allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION);
			}
		};
	}
}
