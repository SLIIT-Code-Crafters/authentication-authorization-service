package com.sep.authenticationauthorization.configuration.utill;

public class UserQueryUtil {

	private static final String SPACE = "\t";

	private UserQueryUtil() {
		throw new IllegalStateException("UserQueryUtil class");
	}

	public static String insertQuery() {
		StringBuilder inserQuery = new StringBuilder();
		inserQuery.append("INSERT INTO \"users\" ");
		inserQuery.append("(");
		inserQuery.append(SPACE + "first_name,");
		inserQuery.append(SPACE + "last_name");
		inserQuery.append(") ");
		inserQuery.append("VALUES (");
		inserQuery.append("?, ?");
		inserQuery.append(") ");

		return inserQuery.toString();
	}

}
