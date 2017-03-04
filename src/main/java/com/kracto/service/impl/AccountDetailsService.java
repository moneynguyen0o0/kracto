package com.kracto.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.data.Role;
import com.kracto.domain.Account;
import com.kracto.dto.LocalUser;
import com.kracto.repository.AccountRepository;

@Service("userDetailsService")
public class AccountDetailsService implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findOne(username);
		if (account == null)
			throw new UsernameNotFoundException("Account not found");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		StringTokenizer tokenizer = new StringTokenizer(account.getRole(), ",");
		while (tokenizer.hasMoreTokens())
			authorities.add(new SimpleGrantedAuthority(Role.PREFIX + tokenizer.nextToken()));
		return new LocalUser(account.getId(), account.getPassword(), account.getPhoto(), account.getEnabled(), true, true,
				true, authorities);
	}
}
