package com.kracto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kracto.domain.Comment;
import com.kracto.dto.AdminComment;
import com.kracto.dto.UserComment;

public interface CommentService extends JdbcService<Comment, Long> {

	Page<AdminComment> findAllOnAdmin(Pageable pageable);

	long countNewState();

	List<UserComment> findByArticleId(Long articleId);

	void addCommentAndUpdateNumComment(Comment comment);
	
	void updateContent(Comment comment);
	
	void updateLiker(Comment comment);
	
	void deleteIdAndAllParentIds(Long id, Long articleId);
	
	void updateState(List<Long> ids);

}
