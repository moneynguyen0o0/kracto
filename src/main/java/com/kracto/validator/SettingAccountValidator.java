package com.kracto.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kracto.data.Gender;
import com.kracto.domain.Account;

@Component
public class SettingAccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Account.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Account account = (Account) target;
		if (account.getFullname() == null)
			errors.rejectValue("gender", "errors.required", "Not empty.");
		if (account.getDob() != null) {
			LocalDate dob = account.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = LocalDate.now();
			long years = ChronoUnit.YEARS.between(dob, end);
			if (years < 10 || years > 100)
				errors.rejectValue("dob", "errors.dob", "Invalid date of birth.");
		}
		if (account.getGender() != null) {
			if (!Gender.exist(account.getGender()))
				errors.rejectValue("gender", "errors.gender", "Invalid gender.");
		}
	}

}