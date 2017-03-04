package com.kracto.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Article;
import com.kracto.repository.generic.JdbcRepository;

public interface ArticleRepository extends JdbcRepository<Article, Long> {
	Page<Article> getTopOnAdmin(Pageable pageable);

	Page<Article> search(String keyword, Pageable pageable);

	boolean existCate(String rootCate);

	boolean existCate(String rootCate, String subCate);

	List<Article> findByRootCate(String rootCate);

	List<Article> findBySubCate(String rootCate, String subCate);

	Page<Article> getTopOnEnUser(Pageable pageable);

	Page<Article> getTopOnViUser(Pageable pageable);

	Page<Article> findEnByCateWithProc(String rootCate, Pageable pageable);

	Page<Article> findViByCateWithProc(String rootCate, Pageable pageable);

	Page<Article> findEnByCateWithProc(String rootCate, String subCate, Pageable pageable);

	Page<Article> findViByCateWithProc(String rootCate, String subCate, Pageable pageable);

	Page<Article> searchEnWithProc(String keyword, Pageable pageable);

	Page<Article> searchViWithProc(String keyword, Pageable pageable);

	Page<Article> searchEnWithProc(String keyword, String rootCate, Pageable pageable);

	Page<Article> searchViWithProc(String keyword, String rootCate, Pageable pageable);

	Article findEnByIdWithProc(Long id);

	Article findViByIdWithProc(Long id);

	List<Article> findEnRelatedWithProc(Long id, String rootCate, String subCate);

	List<Article> findViRelatedWithProc(Long id, String rootCate, String subCate);
	
	void updateViewer(Long id);
}