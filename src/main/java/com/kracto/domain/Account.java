package com.kracto.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Persistable;

public class Account implements Persistable<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4810407619275714127L;
	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	@Size(min = 3, max = 25, message = "{errors.range}")
	private String username;
	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	@Size(min = 7, max = 100, message = "{errors.range}")
	private String password;
	@NotNull(message = "{errors.required}")
	@Email(message = "{errors.email}")
	@Size(max = 50, message = "{errors.range.max}")
	private String email;
	private String fullname;
	private Date dob;
	private String photo;
	private String gender;
	private Boolean enabled;
	private String role;
	private Date created;
	private Date updated;
	private String token;
	private Boolean state;

	private transient boolean persisted;

	@Override
	public String getId() {
		return username;
	}

	@Override
	public boolean isNew() {
		return !persisted;
	}

	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}

	public Account() {
	}

	public Account(String username, String password, String email, String fullname, Date dob, String photo,
			String gender, Boolean enabled, String role, Date created, Date updated, String token, Boolean state) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.dob = dob;
		this.photo = photo;
		this.gender = gender;
		this.enabled = enabled;
		this.role = role;
		this.created = created;
		this.updated = updated;
		this.token = token;
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

}