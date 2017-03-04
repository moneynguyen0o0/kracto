package com.kracto.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kracto.domain.Account;
import com.kracto.repository.AccountRepository;
import com.kracto.repository.generic.JdbcRepositoryImpl;
import com.kracto.repository.generic.RowUnmapper;

@Repository("accountRepository")
public class AccountRepositoryImpl extends JdbcRepositoryImpl<Account, String> implements AccountRepository {

	public AccountRepositoryImpl() {
		super(ROW_MAPPER, ROW_UNMAPPER, "account", "username");
	}

	public static final RowMapper<Account> ROW_MAPPER = new RowMapper<Account>() {

		@Override
		public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Account(rs.getString("username"), rs.getString("password"), rs.getString("email"),
					rs.getString("fullname"), rs.getDate("date_of_birth"), rs.getString("photo"),
					rs.getString("gender"), rs.getBoolean("enabled"), rs.getString("role"), rs.getDate("created"),
					rs.getDate("updated"), rs.getString("token"), rs.getBoolean("state"));
		}
	};

	private static final RowUnmapper<Account> ROW_UNMAPPER = new RowUnmapper<Account>() {

		@Override
		public Map<String, Object> mapColumns(Account account) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("username", account.getId());
			mapping.put("password", account.getPassword());
			mapping.put("email", account.getEmail());
			mapping.put("fullname", account.getFullname());
			mapping.put("date_of_birth", account.getDob());
			mapping.put("photo", account.getPhoto());
			mapping.put("gender", account.getGender());
			mapping.put("enabled", account.getEnabled());
			mapping.put("role", account.getRole());
			mapping.put("created", account.getCreated());
			mapping.put("updated", account.getUpdated());
			mapping.put("token", account.getToken());
			mapping.put("state", account.getState());
			return mapping;
		}
	};

	@Override
	public Account findByEmail(String email) {
		return findByField("email", email);
	}

	@Override
	public Account findByToken(String token) {
		return findByField("token", token);
	}

	private Account findByField(String field, String param) {
		String query = "SELECT * FROM " + getTable().getName() + " WHERE " + field + " = ?";
		String[] params = new String[] { param };
		// queryForObject() throws EmptyResultDataAccessException when record
		// not found
		try {
			return getJdbcTemplate().queryForObject(query, params, ROW_MAPPER);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	String FIELD_SELECT = "username,email,fullname,date_of_birth,gender,enabled,role,created,state";

	@Override
	public Page<Account> findAllDESC(Pageable pageable) {
		return getByFields(null, null, pageable, new BeanPropertyRowMapper<>(Account.class), FIELD_SELECT);
	}

	@Override
	public Page<Account> search(String keyword, Pageable pageable) {
		String filter = " WHERE MATCH(username,email,fullname) AGAINST(?)";
		String[] params = new String[] { keyword };
		return getByFields(filter, params, pageable, new BeanPropertyRowMapper<>(Account.class), FIELD_SELECT);
	}

	@Override
	public long countNewState() {
		String query = "SELECT count(*) FROM " + getTable().getName() + " WHERE state = false";
		return count(query, null);
	}

	@Override
	public void updateState(List<String> usernames) {
		String query = "UPDATE " + getTable().getName() + " SET state = 1 WHERE username = ?";
		if (usernames.size() != 0) {
			getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, usernames.get(i));
				}

				@Override
				public int getBatchSize() {
					return usernames.size();
				}

			});
		}
	}

}