package com.kracto.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kracto.domain.Category;
import com.kracto.repository.CategoryRepository;
import com.kracto.repository.generic.JdbcRepositoryImpl;
import com.kracto.repository.generic.RowUnmapper;

@Repository("categoryRepository")
public class CategoryRepositoryImpl extends JdbcRepositoryImpl<Category, Integer> implements CategoryRepository {

	public CategoryRepositoryImpl() {
		super(ROW_MAPPER, ROW_UNMAPPER, "category");
	}

	public static final RowMapper<Category> ROW_MAPPER = new RowMapper<Category>() {
		@Override
		public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Category(rs.getInt("id"), rs.getString("name_en"), rs.getString("name_vi"), rs.getInt("parent"));
		}
	};

	private static final RowUnmapper<Category> ROW_UNMAPPER = new RowUnmapper<Category>() {
		@Override
		public Map<String, Object> mapColumns(Category category) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", category.getId());
			mapping.put("name_en", category.getNameEn());
			mapping.put("name_vi", category.getNameVi());
			mapping.put("parent", category.getParent());
			return mapping;
		}
	};

	@Override
	public List<Category> findByRootAndParent(int parent) {
		String query = "SELECT * FROM " + getTable().getName() + " WHERE parent = 0 OR parent = ? ORDER BY id";
		Integer[] params = new Integer[] { parent };
		return getJdbcTemplate().query(query, params, ROW_MAPPER);
	}

	@Override
	public long countSub(int rootId) {
		return count("SELECT COUNT(*) FROM " + getTable().getName() + " WHERE parent = ?", new Integer[] { rootId });
	}

	@Override
	public Map<String, String> getByRoot() {
		return getJdbcTemplate().query("SELECT name_en, name_vi FROM " + getTable().getName() + " WHERE parent = 0",
				new ResultSetExtractor<Map<String, String>>() {
					@Override
					public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, String> mapRet = new HashMap<String, String>();
						while (rs.next())
							mapRet.put(rs.getString("name_en"), rs.getString("name_vi"));
						return mapRet;
					}
				});
	}
}