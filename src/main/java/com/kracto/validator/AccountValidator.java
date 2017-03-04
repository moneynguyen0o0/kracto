package com.kracto.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kracto.domain.Account;
import com.kracto.service.AccountService;

@Component
public class AccountValidator implements Validator {

	@Autowired
	private AccountService accountService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Account.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Account account = (Account) target;
		if (accountService.findOne(account.getUsername()) != null)
			errors.rejectValue("username", "errors.username.existing", "Username already exists.");
		if (accountService.findByEmail(account.getEmail()) != null)
			errors.rejectValue("email", "errors.email.existing", "Email already exists.");
	}

}