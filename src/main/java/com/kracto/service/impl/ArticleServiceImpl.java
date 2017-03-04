package com.kracto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.domain.Article;
import com.kracto.repository.ArticleRepository;
import com.kracto.service.ArticleService;
import com.kracto.util.Utils;

@Service("articleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ArticleServiceImpl extends JdbcServiceImpl<Article, Long> implements ArticleService {

	private ArticleRepository articleRepository;

	@Autowired
	public ArticleServiceImpl(ArticleRepository articleRepository) {
		super(articleRepository);
		this.articleRepository = articleRepository;
	}

	@Override
	public Page<Article> getTopOnAdmin(Pageable pageable) {
		return articleRepository.getTopOnAdmin(pageable);
	}

	@Override
	public Page<Article> search(String keyword, Pageable pageable) {
		return articleRepository.search(keyword, pageable);
	}

	@Override
	public boolean existCate(String rootCate) {
		return articleRepository.existCate(rootCate);
	}

	@Override
	public boolean existCate(String rootCate, String subCate) {
		return articleRepository.existCate(rootCate, subCate);
	}

	@Override
	public List<Article> findByRootCate(String rootCate) {
		return articleRepository.findByRootCate(rootCate);
	}

	@Override
	public List<Article> findBySubCate(String rootCate, String subCate) {
		return articleRepository.findBySubCate(rootCate, subCate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void update(List<Article> articles) {
		articleRepository.update(articles);
	}

	@Override
	public Page<Article> getTopOnUser(Pageable pageable) {
		if (Utils.isVi())
			return articleRepository.getTopOnViUser(pageable);
		else
			return articleRepository.getTopOnEnUser(pageable);
	}

	@Override
	public Page<Article> findByCateWithProc(String rootCate, Pageable pageable) {
		if (Utils.isVi())
			return articleRepository.findViByCateWithProc(rootCate, pageable);
		else
			return articleRepository.findEnByCateWithProc(rootCate, pageable);
	}

	@Override
	public Page<Article> findByCateWithProc(String rootCate, String subCate, Pageable pageable) {
		if (Utils.isVi())
			return articleRepository.findViByCateWithProc(rootCate, subCate, pageable);
		else
			return articleRepository.findEnByCateWithProc(rootCate, subCate, pageable);
	}

	@Override
	public Page<Article> searchWithProc(String keyword, Pageable pageable) {
		if (Utils.isVi())
			return articleRepository.searchViWithProc(keyword, pageable);
		else
			return articleRepository.searchEnWithProc(keyword, pageable);
	}

	@Override
	public Page<Article> searchWithProc(String keyword, String rootCate, Pageable pageable) {
		if (Utils.isVi())
			return articleRepository.searchViWithProc(keyword, rootCate, pageable);
		else
			return articleRepository.searchEnWithProc(keyword, rootCate, pageable);
	}

	@Override
	public Article findByIdWithProc(Long id) {
		if (Utils.isVi())
			return articleRepository.findViByIdWithProc(id);
		else
			return articleRepository.findEnByIdWithProc(id);
	}

	@Override
	public List<Article> findRelatedWithProc(Long id, String rootCate, String subCate) {
		if (Utils.isVi())
			return articleRepository.findViRelatedWithProc(id, rootCate, subCate);
		else
			return articleRepository.findEnRelatedWithProc(id, rootCate, subCate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateViewer(Long id) {
		articleRepository.updateViewer(id);
	}

}