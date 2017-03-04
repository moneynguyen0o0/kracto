package com.kracto.repository;

import java.util.List;
import java.util.Map;

import com.kracto.domain.Category;
import com.kracto.repository.generic.JdbcRepository;

public interface CategoryRepository extends JdbcRepository<Category, Integer> {
	
	List<Category> findByRootAndParent(int parent);

	long countSub(int rootId);
	
	Map<String, String> getByRoot();
	
}
