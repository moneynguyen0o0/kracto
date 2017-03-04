package com.kracto.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kracto.data.Offset;
import com.kracto.domain.Article;
import com.kracto.repository.ArticleRepository;
import com.kracto.repository.generic.JdbcRepositoryImpl;
import com.kracto.repository.generic.RowUnmapper;

@Repository("articleRepository")
public class ArticleRepositoryImpl extends JdbcRepositoryImpl<Article, Long> implements ArticleRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

	public ArticleRepositoryImpl() {
		super(ROW_MAPPER, ROW_UNMAPPER, "article");
	}

	public static final RowMapper<Article> ROW_MAPPER = new RowMapper<Article>() {
		@Override
		public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Article(rs.getLong("id"), rs.getString("title_en"), rs.getString("title_vi"),
					rs.getString("description_en"), rs.getString("description_vi"), rs.getString("small_image"),
					rs.getString("large_image"), rs.getInt("viewer"), rs.getInt("comment"), rs.getString("username"),
					rs.getString("root_cate"), rs.getString("sub_cate"), rs.getDate("created"), rs.getDate("updated"));
		}
	};

	private static final RowUnmapper<Article> ROW_UNMAPPER = new RowUnmapper<Article>() {
		@Override
		public Map<String, Object> mapColumns(Article article) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", article.getId());
			mapping.put("title_en", article.getTitleEn());
			mapping.put("title_vi", article.getTitleVi());
			mapping.put("description_en", article.getDescriptionEn());
			mapping.put("description_vi", article.getDescriptionVi());
			mapping.put("small_image", article.getSmallImage());
			mapping.put("large_image", article.getLargeImage());
			mapping.put("viewer", article.getViewer());
			mapping.put("comment", article.getComment());
			mapping.put("username", article.getUsername());
			mapping.put("root_cate", article.getRootCate());
			mapping.put("sub_cate", article.getSubCate());
			mapping.put("created", article.getCreated());
			mapping.put("updated", article.getUpdated());
			return mapping;
		}
	};

	private static final String FIELD_SELECTING_ADMIN = "id,title_en,root_cate,viewer,comment,created,updated,username";

	@Override
	public Page<Article> getTopOnAdmin(Pageable pageable) {
		return getByFields(null, null, pageable, new BeanPropertyRowMapper<>(Article.class), FIELD_SELECTING_ADMIN);
	}

	@Override
	public Page<Article> search(String keyword, Pageable pageable) {
		String filter = " WHERE MATCH(title_en,title_vi,description_en,description_vi,username) AGAINST(?)";
		String[] params = new String[] { keyword };
		return getByFields(filter, params, pageable, new BeanPropertyRowMapper<>(Article.class), FIELD_SELECTING_ADMIN);
	}

	@Override
	public boolean existCate(String rootCate) {
		String query = "SELECT EXISTS(SELECT 1 FROM " + getTable().getName() + " WHERE root_cate = ? LIMIT 1)";
		String[] params = new String[] { rootCate };
		return exists(query, params);
	}

	@Override
	public boolean existCate(String rootCate, String subCate) {
		String query = "SELECT EXISTS(SELECT 1 FROM " + getTable().getName()
				+ " WHERE root_cate = ? AND JSON_CONTAINS(sub_cate, ?) LIMIT 1)";
		String[] params = new String[] { rootCate, subCate };
		return exists(query, params);
	}

	@Override
	public List<Article> findByRootCate(String rootCate) {
		return getByFields(" WHERE root_cate = ?", new String[] { rootCate }, ROW_MAPPER, "*");
	}

	@Override
	public List<Article> findBySubCate(String rootCate, String subCate) {
		return getByFields(" WHERE root_cate = ? AND JSON_CONTAINS(sub_cate, ?)", new String[] { rootCate, subCate },
				ROW_MAPPER, "*");
	}

	@Override
	@Cacheable(value = "getTopOnEnUser", key = "#pageable")
	public Page<Article> getTopOnEnUser(Pageable pageable) {
		LOGGER.info("getTopOnEnUser cache is running....");

		return getTopOnUser("get_top_en_articles", pageable);
	}

	@Override
	@Cacheable(value = "getTopOnViUser", key = "#pageable")
	public Page<Article> getTopOnViUser(Pageable pageable) {
		LOGGER.info("getTopOnViUser cache is running....");

		return getTopOnUser("get_top_vi_articles", pageable);
	}

	private Page<Article> getTopOnUser(String procedureName, Pageable pageable) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_offset", getSqlGenerator().calOffset(pageable));
		inParam.put("_size", pageable.getPageSize());
		return callProcedureMethod(procedureName, pageable, inParam);
	}

	@Override
	@Cacheable(value = "findEnByRootCateWithProc", key = "{ #rootCate, #pageable} ")
	public Page<Article> findEnByCateWithProc(String rootCate, Pageable pageable) {
		LOGGER.info("findEnByRootCateWithProc cache is running....");

		return findByCateWithProc("find_en_articles_by_root_cate", rootCate, null, pageable);
	}

	@Override
	@Cacheable(value = "findViByRootCateWithProc", key = "{ #rootCate, #pageable} ")
	public Page<Article> findViByCateWithProc(String rootCate, Pageable pageable) {
		LOGGER.info("findViByRootCateWithProc cache is running....");

		return findByCateWithProc("find_vi_articles_by_root_cate", rootCate, null, pageable);
	}

	@Override
	@Cacheable(value = "findEnBySubCateWithProc", key = "{ #rootCate, #subCate, #pageable} ")
	public Page<Article> findEnByCateWithProc(String rootCate, String subCate, Pageable pageable) {
		LOGGER.info("findEnBySubCateWithProc cache is running....");

		return findByCateWithProc("find_en_articles_by_sub_cate", rootCate, subCate, pageable);
	}

	@Override
	@Cacheable(value = "findViBySubCateWithProc", key = "{ #rootCate, #subCate, #pageable} ")
	public Page<Article> findViByCateWithProc(String rootCate, String subCate, Pageable pageable) {
		LOGGER.info("findViBySubCateWithProc cache is running....");

		return findByCateWithProc("find_vi_articles_by_sub_cate", rootCate, subCate, pageable);
	}

	private Page<Article> findByCateWithProc(String procedureName, String rootCate, String subCate, Pageable pageable) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_root", rootCate);
		if (subCate != null)
			inParam.put("_sub", subCate);
		inParam.put("_offset", getSqlGenerator().calOffset(pageable));
		inParam.put("_size", pageable.getPageSize());
		return callProcedureMethod(procedureName, pageable, inParam);
	}

	private Page<Article> callProcedureMethod(String procedureName, Pageable pageable, Map<String, Object> inParam) {
		return getByProcedure(procedureName, inParam, "_total", BeanPropertyRowMapper.newInstance(Article.class),
				pageable);
	}

	@Override
	public Page<Article> searchEnWithProc(String keyword, Pageable pageable) {
		return searchWithProc("search_en_articles", keyword, null, pageable);
	}

	@Override
	public Page<Article> searchViWithProc(String keyword, Pageable pageable) {
		return searchWithProc("search_vi_articles", keyword, null, pageable);
	}

	@Override
	public Page<Article> searchEnWithProc(String keyword, String rootCate, Pageable pageable) {
		return searchWithProc("search_en_articles_with_root_cate", keyword, rootCate, pageable);
	}

	@Override
	public Page<Article> searchViWithProc(String keyword, String rootCate, Pageable pageable) {
		return searchWithProc("search_vi_articles_with_root_cate", keyword, rootCate, pageable);
	}

	private Page<Article> searchWithProc(String procedureName, String keyword, String rootCate, Pageable pageable) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_keyword", keyword);
		if (rootCate != null)
			inParam.put("_root", rootCate);
		inParam.put("_offset", getSqlGenerator().calOffset(pageable));
		inParam.put("_size", pageable.getPageSize());
		return callProcedureMethod(procedureName, pageable, inParam);
	}

	@Override
	@Cacheable(value = "findEnByIdWithProc", key = "#id")
	public Article findEnByIdWithProc(Long id) {
		LOGGER.info("findEnByIdWithProc cache is running....");

		return findByIdWithProc("find_en_articles_by_id", id);
	}

	@Override
	@Cacheable(value = "findViByIdWithProc", key = "#id")
	public Article findViByIdWithProc(Long id) {
		LOGGER.info("findViByIdWithProc cache is running....");

		return findByIdWithProc("find_vi_articles_by_id", id);
	}

	private Article findByIdWithProc(String procedureName, Long id) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_id", id);
		return findOneByProcedure(procedureName, inParam, BeanPropertyRowMapper.newInstance(Article.class));
	}

	@Override
	@Cacheable(value = "findEnRelatedWithProc", key = "{ #id, #rootCate, #subCate} ")
	public List<Article> findEnRelatedWithProc(Long id, String rootCate, String subCate) {
		LOGGER.info("findEnRelatedWithProc cache is running....");

		return findRelatedWithProc("get_en_related_articles", id, rootCate, subCate);
	}

	@Override
	@Cacheable(value = "findViRelatedWithProc", key = "{ #id, #rootCate, #subCate} ")
	public List<Article> findViRelatedWithProc(Long id, String rootCate, String subCate) {
		LOGGER.info("findViRelatedWithProc cache is running....");

		return findRelatedWithProc("get_vi_related_articles", id, rootCate, subCate);
	}

	private List<Article> findRelatedWithProc(String procedureName, Long id, String rootCate, String subCate) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_id", id);
		inParam.put("_root", rootCate);
		inParam.put("_sub", subCate);
		inParam.put("_offset", Offset.RELATED_ARTICLE);
		return getByProcedure(procedureName, inParam, BeanPropertyRowMapper.newInstance(Article.class));
	}

	@Override
	public void updateViewer(Long id) {
		String query = "UPDATE " + getTable().getName() + " SET viewer = viewer + 1 WHERE id = ?";
		getJdbcTemplate().update(query, new Object[] { id });
	}

}