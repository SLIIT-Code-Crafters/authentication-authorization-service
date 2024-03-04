package com.sep.authenticationauthorization.configuration.enums;

public enum Status {
	W("WHITELISTED"), B("BLACKLISTED"), I("INACTIVE");

	private String status;

	private Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
