package com.kracto.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.domain.Account;
import com.kracto.dto.NewPassword;
import com.kracto.repository.AccountRepository;
import com.kracto.service.AccountService;
import com.kracto.util.SecurityUtils;

@Service("accountService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AccountServiceImpl extends JdbcServiceImpl<Account, String> implements AccountService {

	private AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		super(accountRepository);
		this.accountRepository = accountRepository;
	}

	@Override
	public Account findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	@Override
	public Account findEnabledByEmail(String email) {
		Account account = accountRepository.findByEmail(email);
		if (account != null && account.getEnabled())
			return account;
		return null;
	}

	@Override
	public Account findByToken(String token) {
		return accountRepository.findByToken(token);
	}

	@Override
	public Account findUnEnabledByToken(String token) {
		Account account = accountRepository.findByToken(token);
		if (account != null && !account.getEnabled())
			return account;
		return null;
	}

	@Override
	public Page<Account> findAllDESC(Pageable pageable) {
		return accountRepository.findAllDESC(pageable);
	}

	@Override
	public Page<Account> search(String keyword, Pageable pageable) {
		return accountRepository.search(keyword, pageable);
	}

	@Override
	public long countNewState() {
		return accountRepository.countNewState();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void changePassword(NewPassword newPassword) {
		Account account = accountRepository.findOne(SecurityUtils.getUsername());
		if (account != null) {
			account.setPassword(SecurityUtils.encodePassword(newPassword.getNewPassword()));
			accountRepository.update(account);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateMoreInfo(Account oldAccount, Account newAccount) {
		oldAccount.setFullname(newAccount.getFullname());
		oldAccount.setDob(newAccount.getDob());
		oldAccount.setGender(newAccount.getGender());
		if (newAccount.getPhoto() != null)
			oldAccount.setPhoto(newAccount.getPhoto());
		oldAccount.setUpdated(new Date());
		accountRepository.update(oldAccount);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateState(List<String> usernames) {
		accountRepository.updateState(usernames);
	}

}