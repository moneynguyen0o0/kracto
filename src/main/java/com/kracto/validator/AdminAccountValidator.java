package com.kracto.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.kracto.data.Role;
import com.kracto.domain.Account;

@Component
public class AdminAccountValidator extends AccountValidator {

	@Override
	public void validate(Object target, Errors errors) {
		Account account = (Account) target;
		boolean check = false;
		if (account.getRole() != null) {
			for (String role : Role.getRoles())
				if (role.equals(account.getRole().toUpperCase()))
					check = true;
		}
		if (!check)
			errors.rejectValue("role", "errors.required", "Role is required.");
		super.validate(target, errors);
	}

}