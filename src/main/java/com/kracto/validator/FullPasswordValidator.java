package com.kracto.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.kracto.dto.NewPassword;
import com.kracto.util.SecurityUtils;

@Component
public class FullPasswordValidator extends ConfirmPasswordValidator {

	@Override
	public void validate(Object target, Errors errors) {
		NewPassword newPassword = (NewPassword) target;
		if (!SecurityUtils.getAuthentication().getCredentials().toString().equals(newPassword.getCurrentPassword()))
			errors.rejectValue("currentPassword", "errors.password.current", "Not match.");
		super.validate(target, errors);
	}

}
