package com.kracto.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Password {

	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	@Size(min = 7, max = 100, message = "{errors.range}")
	private String newPassword;
	@NotNull(message = "{errors.required}")
	@NotEmpty(message = "{errors.required}")
	@Size(min = 7, max = 100, message = "{errors.range}")
	private String confirmPassword;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
