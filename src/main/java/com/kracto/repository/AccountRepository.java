package com.kracto.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Account;
import com.kracto.repository.generic.JdbcRepository;

public interface AccountRepository extends JdbcRepository<Account, String> {

	Page<Account> findAllDESC(Pageable pageable);

	Page<Account> search(String keyword, Pageable pageable);

	Account findByEmail(String email);

	Account findByToken(String token);

	long countNewState();

	void updateState(List<String> usernames);

}
