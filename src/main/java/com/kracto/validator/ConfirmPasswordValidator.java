package com.kracto.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kracto.dto.Password;

@Component
public class ConfirmPasswordValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Password.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Password password = (Password) target;
		if (!password.getNewPassword().equals(password.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "errors.password.confirm", "Not match.");
	}

}
