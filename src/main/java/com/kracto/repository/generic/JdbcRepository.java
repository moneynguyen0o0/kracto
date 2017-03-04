package com.kracto.repository.generic;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JdbcRepository<T, ID extends Serializable> {
	<S extends T> S save(S entity);

	void save(List<T> entities);
	
	<S extends T> S update(S entity);
	
	void update(List<T> entities);

	T findOne(ID id);

	boolean exists(ID id);

	List<T> findAll();

	List<T> findAll(List<ID> ids);

	List<T> findAll(Sort sort);

	Page<T> findAll(Pageable pageable);

	long count();

	void delete(ID id);

	void delete(T entity);

	void deleteAll();
}
