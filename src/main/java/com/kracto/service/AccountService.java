package com.kracto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Account;
import com.kracto.dto.NewPassword;

public interface AccountService extends JdbcService<Account, String> {

	Page<Account> findAllDESC(Pageable pageable);

	Page<Account> search(String keyword, Pageable pageable);

	Account findByEmail(String email);
	
	Account findEnabledByEmail(String email);
	
	Account findByToken(String token);
	
	Account findUnEnabledByToken(String token);

	long countNewState();

	void changePassword(NewPassword newPassword);

	void updateMoreInfo(Account oldAccount, Account newAccount);

	void updateState(List<String> usernames);

}
