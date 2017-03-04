package com.kracto.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class NewPassword extends Password {

	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	private String currentPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

}
