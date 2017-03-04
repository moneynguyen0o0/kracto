package com.kracto.data;

import java.util.ArrayList;
import java.util.List;

public final class Role {

	public static final String PREFIX = "ROLE_";
	public static final String SUPERADMIN = "SUPERADMIN";
	public static final String ADMIN = "ADMIN";
	public static final String MEMBER = "MEMBER";

	public static List<String> getRoles() {
		List<String> roles = new ArrayList<String>();
		roles.add(ADMIN);
		roles.add(SUPERADMIN);
		return roles;
	}

}
