package com.sep.authenticationauthorization.configuration.utill;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CommonUtils {

	public static String convertToString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getExecutionTime(long startTime) {
		long endTime = System.currentTimeMillis();
		return String.format("%d ms", (endTime - startTime));
	}

}
