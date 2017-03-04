package com.kracto.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Comment;
import com.kracto.dto.AdminComment;
import com.kracto.repository.generic.JdbcRepository;

public interface CommentRepository extends JdbcRepository<Comment, Long> {

	Page<AdminComment> findAllOnAdmin(Pageable pageable);

	long countNewState();

	List<Comment> findByArticleId(Long articleId);

	void updateContent(Long id, String content, Date updated);
	
	void updateLiker(Long id, String liker);
	
	void deleteAllParentIds(Long id);
	
	void updateState(List<Long> ids);

}
