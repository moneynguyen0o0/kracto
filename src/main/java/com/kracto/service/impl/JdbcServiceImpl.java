package com.kracto.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.repository.generic.JdbcRepository;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public abstract class JdbcServiceImpl<T extends Persistable<ID>, ID extends Serializable>
		implements JdbcRepository<T, ID> {

	private JdbcRepository<T, ID> jdbcRepository;

	public JdbcServiceImpl(JdbcRepository<T, ID> jdbcRepository) {
		this.jdbcRepository = jdbcRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public <S extends T> S save(S entity) {
		return jdbcRepository.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void save(List<T> entities) {
		jdbcRepository.save(entities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public <S extends T> S update(S entity) {
		return jdbcRepository.update(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void update(List<T> entities) {
		jdbcRepository.update(entities);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void delete(ID id) {
		jdbcRepository.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void delete(T entity) {
		jdbcRepository.delete(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteAll() {
		jdbcRepository.deleteAll();
	}

	@Override
	public List<T> findAll() {
		return jdbcRepository.findAll();
	}

	@Override
	public List<T> findAll(List<ID> ids) {
		return jdbcRepository.findAll(ids);
	}

	@Override
	public List<T> findAll(Sort sort) {
		return jdbcRepository.findAll(sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return jdbcRepository.findAll(pageable);
	}

	@Override
	public T findOne(ID id) {
		return jdbcRepository.findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return jdbcRepository.exists(id);
	}

	@Override
	public long count() {
		return jdbcRepository.count();
	}

}
