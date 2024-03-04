package com.sep.authenticationauthorization.configuration.enums;

public enum Roles {
	SA("SYSTEM_ADMIN"), TO("TRIP_ORGANIZER"), TP("PARTICIPANT");

	private String role;

	private Roles(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
