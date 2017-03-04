package com.kracto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Article;

public interface ArticleService extends JdbcService<Article, Long> {
	void update(List<Article> articles);

	Page<Article> getTopOnAdmin(Pageable pageable);

	Page<Article> search(String keyword, Pageable pageable);

	boolean existCate(String rootCate);

	boolean existCate(String rootCate, String subCate);

	List<Article> findByRootCate(String rootCate);

	List<Article> findBySubCate(String rootCate, String subCate);

	Page<Article> getTopOnUser(Pageable pageable);

	Page<Article> findByCateWithProc(String rootCate, Pageable pageable);

	Page<Article> findByCateWithProc(String rootCate, String subCate, Pageable pageable);

	Page<Article> searchWithProc(String keyword, Pageable pageable);

	Page<Article> searchWithProc(String keyword, String rootCate, Pageable pageable);

	Article findByIdWithProc(Long id);

	List<Article> findRelatedWithProc(Long id, String rootCate, String subCate);
	
	void updateViewer(Long id);
}