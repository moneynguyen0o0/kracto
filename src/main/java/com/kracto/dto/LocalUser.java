package com.kracto.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LocalUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6583529566496752430L;
	
	private String photo;

	public LocalUser(final String username, final String password, final String photo, final boolean enabled,
			final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
			final Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.photo = photo;
	}

	public String getPhoto() {
		return photo;
	}
}