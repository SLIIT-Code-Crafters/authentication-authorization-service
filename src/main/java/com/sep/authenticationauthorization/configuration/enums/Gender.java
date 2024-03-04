package com.sep.authenticationauthorization.configuration.enums;

public enum Gender {
	M("MALE"), F("FEMALE"), O("OTHER");

	private String gender;

	private Gender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
