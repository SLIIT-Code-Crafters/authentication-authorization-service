package com.sep.authenticationauthorization.configuration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration	
public class CorsConfig  implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.exposedHeaders("*")
				.allowedOriginPatterns("*")
				.allowedHeaders("*")
				.maxAge(3600)
				.allowedMethods("*");
	}
	
//	@Bean
//	public WebMvcConfigurer corsConfigure() {
//		return new WebMvcConfigurer() {
//
//			@Override
//			public void addCorsMappings(final CorsRegistry registry) {
//				registry.addMapping("/**")
//						.allowCredentials(true)
//						.allowedOrigins("http://localhost:4200","http://travel-trek-angular-client.s3-website-ap-southeast-1.amazonaws.com")
//						.allowedMethods(HttpMethod.GET.name(),HttpMethod.POST.name(),HttpMethod.PUT.name(),HttpMethod.DELETE.name(),HttpMethod.PATCH.name(),HttpMethod.OPTIONS.name())
//						.allowedHeaders("*")
//                        .maxAge(3600);
//			}
//		};
//	}
//	
}
