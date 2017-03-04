package com.kracto.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.domain.Category;
import com.kracto.dto.RootCate;
import com.kracto.repository.CategoryRepository;
import com.kracto.service.CategoryService;

@Service("categoryService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CategoryServiceImpl extends JdbcServiceImpl<Category, Integer> implements CategoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super(categoryRepository);
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> findByRootAndParent(int parent) {
		return categoryRepository.findByRootAndParent(parent);
	}

	@Override
	public long countSub(int rootId) {
		return categoryRepository.countSub(rootId);
	}

	@Override
	@Cacheable(value = "getCategoriesByRootMap")
	public Map<String, RootCate> getByRoot() {
		LOGGER.info("getCategoriesByRootMap cache is running....");
		
		Map<String, RootCate> result = new HashMap<String, RootCate>();
		int i = 1;
		for (Entry<String, String> entry : categoryRepository.getByRoot().entrySet()) {
			String nameEn = entry.getKey();
			String nameVi = entry.getValue();
			result.put(nameEn, new RootCate(nameEn, nameVi, i++));
		}
		return result;
	}

	@Override
	@CacheEvict("getCategoriesByRootMap")
	public void clearCategoriesByRootMapCache() {
		LOGGER.info("Clear cache named getCategoriesByRootMap....");
	}

}
