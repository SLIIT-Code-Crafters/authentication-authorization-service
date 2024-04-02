package com.sep.authenticationauthorization.configuration.utill;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sep.authenticationauthorization.configuration.dto.UserDto;
import com.sep.authenticationauthorization.configuration.dto.authentication.AuthenticationRequest;
import com.sep.authenticationauthorization.configuration.dto.login.LoginRequest;
import com.sep.authenticationauthorization.configuration.enums.Roles;
import com.sep.authenticationauthorization.configuration.enums.Salutation;

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

	public static boolean isNumberLength(Integer number) {
		if (number == null) {
			return false;
		} else {
			if (String.valueOf(number).length() == 9) {
				return true;
			}
		}
		return false;
	}

	public static boolean haveEmptySpace(String string) {
		Pattern whitespace = Pattern.compile("\\s");
		Matcher matcher = whitespace.matcher(string);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean containsOnlyLetters(String str) {
		String regex = "^[a-zA-Z]*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return (matcher.matches() && !haveEmptySpace(str));
	}

	public static boolean validatePhoneNumber(String phoneNo) {
		return (isNumberLength(phoneNo, 10) && isNumeric(phoneNo) && isFirstDigitZero(phoneNo));

	}

	public static boolean isNumberLength(String number, int length) {
		return (number.length() == length);
	}

	public static boolean isNumeric(String stringNumber) {
		Pattern numeric = Pattern.compile(".*[^0-9].*");
		Matcher digits = numeric.matcher(stringNumber);
		return !digits.find();
	}

	public static boolean isFirstDigitZero(String phoneNo) {
		char firstChar = phoneNo.charAt(0);
		return firstChar == '0';
	}

	public static boolean checkMandtoryFieldsNullOrEmpty(UserDto userDto) {
		return !((userDto.getFirstName() == null || userDto.getFirstName().isEmpty() || userDto.getFirstName().isBlank()
				|| userDto.getFirstName().equals(""))
				|| (userDto.getLastName() == null || userDto.getLastName().isEmpty() || userDto.getLastName().isBlank()
						|| userDto.getLastName().equals(""))
				|| (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()
						|| userDto.getEmail().equals(""))
				|| (userDto.getUserName() == null || userDto.getUserName().isEmpty() || userDto.getUserName().isBlank()
						|| userDto.getUserName().equals(""))
				|| (userDto.getSalutation() == null || userDto.getSalutation().isEmpty()
						|| userDto.getSalutation().isBlank() || userDto.getSalutation().equals(""))
				|| (userDto.getContactNo() == null || userDto.getContactNo().isEmpty()
						|| userDto.getContactNo().isBlank() || userDto.getContactNo().equals(""))
				|| (userDto.getRole() == null || userDto.getRole().isEmpty() || userDto.getRole().isBlank()
						|| userDto.getRole().equals(""))
				|| (userDto.getPassword() == null || userDto.getPassword().isEmpty() || userDto.getPassword().isBlank()
						|| userDto.getPassword().equals("")));
	}

	public static boolean checkMasterTokenNullOrEmpty(String masterToken) {
		return !((masterToken == null || masterToken.isEmpty() || masterToken.isBlank() || masterToken.equals("")));
	}

	public static boolean checkAuthMandtoryFieldsNullOrEmpty(AuthenticationRequest request) {
		return !((request.getEmail() == null || request.getEmail().isEmpty() || request.getEmail().isBlank()
				|| request.getEmail().equals(""))
				|| (request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().isBlank()
						|| request.getPassword().equals("")));
	}

	public static boolean isValidateRole(String role) {
		return (role.equals(Roles.SA.name()) || role.equals(Roles.TO.name()) || role.equals(Roles.TP.name()));
	}

	public static boolean isValidateSalutation(String salutation) {
		return (salutation.equals(Salutation.DR.name()) || salutation.equals(Salutation.HON.name())
				|| salutation.equals(Salutation.MISS.name()) || salutation.equals(Salutation.MR.name())
				|| salutation.equals(Salutation.MRS.name()) || salutation.equals(Salutation.MS.name())
				|| salutation.equals(Salutation.REV.name()));
	}

	public static boolean isValidPassword(String password) {
		/*
		 * Regular expression to validate password Lenght Between 8 - 15 characters
		 * Contains at least 1 special character and 1 capital letter and 1 simple
		 * letter and 1 number
		 */

		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,15}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isValidUserName(String userName) {
		String regex = "^[a-zA-Z0-9_]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(userName);
		return matcher.matches() && !haveEmptySpace(userName);
	}

	public static String isUserNameOrEmail(String input) {

		String emailPattern = "^(.+)@(.+)$";
		String usernamePattern = "^[a-zA-Z0-9_]+$";

		Pattern emailRegex = Pattern.compile(emailPattern);
		Pattern usernameRegex = Pattern.compile(usernamePattern);

		Matcher emailMatcher = emailRegex.matcher(input);
		Matcher usernameMatcher = usernameRegex.matcher(input);

		// Checking if input is an email or username
		if (emailMatcher.matches()) {
			return "EMAIL";
		} else if (usernameMatcher.matches()) {
			return "USERNAME";
		} else {
			return "Invalid Input";
		}
	}

	public static boolean checkUserInputNullOrEmpty(LoginRequest request) {
		return !((request.getUser() == null || request.getUser().isEmpty() || request.getUser().isBlank()
				|| request.getUser().equals(""))
				|| (request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().isBlank()
						|| request.getPassword().equals("")));
	}

	public static String generateActivationCode() {
		Random random = new Random();
		int min = 100000;
		int max = 999999;
		int code = random.nextInt(max - min + 1) + min;
		return String.valueOf(code);
	}

}
