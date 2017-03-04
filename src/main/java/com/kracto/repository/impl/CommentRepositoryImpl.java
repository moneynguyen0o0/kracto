package com.kracto.repository.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kracto.domain.Comment;
import com.kracto.dto.AdminComment;
import com.kracto.repository.CommentRepository;
import com.kracto.repository.generic.JdbcRepositoryImpl;
import com.kracto.repository.generic.RowUnmapper;

@Repository("commentRepository")
public class CommentRepositoryImpl extends JdbcRepositoryImpl<Comment, Long> implements CommentRepository {

	public CommentRepositoryImpl() {
		super(ROW_MAPPER, ROW_UNMAPPER, "comment");
	}

	public static final RowMapper<Comment> ROW_MAPPER = new RowMapper<Comment>() {
		@Override
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Comment(rs.getLong("id"), rs.getLong("article_id"), rs.getString("username"),
					rs.getString("content"), rs.getString("profile_picture_url"), rs.getBoolean("created_by_admin"),
					rs.getString("liker"), rs.getDate("created"), rs.getDate("updated"), rs.getLong("parent_id"),
					rs.getBoolean("state"));
		}
	};

	private static final RowUnmapper<Comment> ROW_UNMAPPER = new RowUnmapper<Comment>() {
		@Override
		public Map<String, Object> mapColumns(Comment comment) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", comment.getId());
			mapping.put("article_id", comment.getArticleId());
			mapping.put("username", comment.getUsername());
			mapping.put("content", comment.getContent());
			mapping.put("profile_picture_url", comment.getProfilePictureUrl());
			mapping.put("created_by_admin", comment.getCreatedByAdmin());
			mapping.put("liker", comment.getLiker());
			mapping.put("created", comment.getCreated());
			mapping.put("updated", comment.getUpdated());
			mapping.put("parent_id", comment.getParentId());
			mapping.put("state", comment.getState());
			return mapping;
		}
	};

	@Override
	protected <S extends Comment> S postCreate(S entity, Number generatedId) {
		entity.setId(generatedId.longValue());
		return entity;
	}

	@Override
	public Page<AdminComment> findAllOnAdmin(Pageable pageable) {
		// http://stackoverflow.com/questions/15328696/conditional-count-in-mysql
		String query = "SELECT SQL_CALC_FOUND_ROWS article_id, sum(state = 0) as non_view, count(*) as total FROM "
				+ getTable().getName() + " GROUP BY article_id"
				+ getSqlGenerator().sortingClauseIfRequired(pageable.getSort())
				+ getSqlGenerator().limitClause(pageable);
		return new PageImpl<AdminComment>(
				getJdbcTemplate().query(query, new BeanPropertyRowMapper<>(AdminComment.class)), pageable,
				getJdbcTemplate().queryForObject("SELECT FOUND_ROWS()", Long.class));
	}

	@Override
	public long countNewState() {
		String query = "SELECT count(*) FROM " + getTable().getName() + " WHERE state = false";
		return count(query, null);
	}

	@Override
	public List<Comment> findByArticleId(Long articleId) {
		Map<String, Object> inParam = new HashMap<String, Object>();
		inParam.put("_id", articleId);
		return getByProcedure("find_comment_with_article_id", inParam,
				BeanPropertyRowMapper.newInstance(Comment.class));
	}

	@Override
	public void updateContent(Long id, String content, Date updated) {
		String query = "UPDATE " + getTable().getName() + " SET content = ?, updated = ? WHERE id= ?";
		Object[] queryParams = { content, updated, id };
		getJdbcTemplate().update(query, queryParams);
	}

	@Override
	public void updateLiker(Long id, String liker) {
		String query = "UPDATE " + getTable().getName() + " SET liker = ? WHERE id= ?";
		getJdbcTemplate().update(query, liker, id);
	}

	@Override
	public void deleteAllParentIds(Long id) {
		String query = "DELETE FROM " + getTable().getName() + " WHERE parent_id = ?";
		getJdbcTemplate().update(query, id);
	}

	@Override
	public void updateState(List<Long> ids) {
		String query = "UPDATE " + getTable().getName() + " SET state = 1 WHERE id = ?";
		if (ids.size() != 0) {
			getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, ids.get(i));
				}

				@Override
				public int getBatchSize() {
					return ids.size();
				}

			});
		}
	}

}