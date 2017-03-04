package com.kracto.service;

import java.util.List;
import java.util.Map;

import com.kracto.domain.Category;
import com.kracto.dto.RootCate;

public interface CategoryService extends JdbcService<Category, Integer> {
	
	List<Category> findByRootAndParent(int parent);

	long countSub(int rootId);
	
	Map<String, RootCate> getByRoot();
	
	void clearCategoriesByRootMapCache();
}
