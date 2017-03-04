package com.kracto.util;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kracto.data.Role;

public final class SecurityUtils {

	public static String encodePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	public static boolean matchesPassword(String oldPassword, String currentPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(oldPassword, currentPassword);
	}

	public static boolean checkRole(String role) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities();
		if (authorities.contains(new SimpleGrantedAuthority(Role.PREFIX + role)))
			return true;
		return false;
	}

	public static boolean isAnonymous() {
		return getAuthentication() instanceof AnonymousAuthenticationToken;
	}

	public static void logout() {
		SecurityContextHolder.clearContext();
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static String getUsername() {
		return getAuthentication().getName();
	}

}
