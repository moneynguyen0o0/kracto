package com.kracto.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kracto.domain.Article;
import com.kracto.domain.Comment;
import com.kracto.dto.AdminComment;
import com.kracto.dto.UserComment;
import com.kracto.repository.ArticleRepository;
import com.kracto.repository.CommentRepository;
import com.kracto.service.CommentService;
import com.kracto.util.SecurityUtils;
import com.kracto.util.Utils;

@Service("commentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CommentServiceImpl extends JdbcServiceImpl<Comment, Long> implements CommentService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

	private CommentRepository commentRepository;
	private ArticleRepository articleRepository;

	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository) {
		super(commentRepository);
		this.commentRepository = commentRepository;
		this.articleRepository = articleRepository;
	}

	@Override
	public Page<AdminComment> findAllOnAdmin(Pageable pageable) {
		return commentRepository.findAllOnAdmin(pageable);
	}

	@Override
	public long countNewState() {
		return commentRepository.countNewState();
	}

	@Override
	@Cacheable(value = "findCommentsByArticleId", key = "#articleId")
	public List<UserComment> findByArticleId(Long articleId) {
		LOGGER.info("findCommentsByArticleId cache is running....");
		
		List<Comment> comments = commentRepository.findByArticleId(articleId);
		List<UserComment> userComments = new ArrayList<UserComment>();
		String username = SecurityUtils.getUsername();
		for (Comment comment : comments) {
			List<String> likers = Utils.parseJson(comment.getLiker());
			userComments.add(new UserComment(comment.getId(), comment.getUsername(), comment.getContent(),
					comment.getProfilePictureUrl(), comment.getCreatedByAdmin(), likers == null ? 0 : likers.size(),
					comment.getCreated(), comment.getUpdated(), comment.getParentId(),
					comment.getUsername().equals(username), Utils.existValueInList(username, likers),
					comment.getState()));
		}
		return userComments;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@CacheEvict(value = "findCommentsByArticleId", key = "#comment?.articleId")
	public void addCommentAndUpdateNumComment(Comment comment) {
		LOGGER.info("Clear cache named findCommentsByArticleId....");
		
		Article article = articleRepository.findOne(comment.getArticleId());
		if (article != null) {
			commentRepository.save(comment);
			article.setComment(article.getComment() + 1);
			articleRepository.update(article);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@CacheEvict(value = "findCommentsByArticleId", key = "#comment?.articleId")
	public void updateContent(Comment comment) {
		LOGGER.info("Clear cache named findCommentsByArticleId....");
		
		commentRepository.updateContent(comment.getId(), comment.getContent(), new Date());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@CacheEvict(value = "findCommentsByArticleId", key = "#articleId")
	public void deleteIdAndAllParentIds(Long id, Long articleId) {
		LOGGER.info("Clear cache named findCommentsByArticleId....");
		
		commentRepository.deleteAllParentIds(id);
		commentRepository.delete(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@CacheEvict(value = "findCommentsByArticleId", key = "#comment?.articleId")
	public void updateLiker(Comment comment) {
		LOGGER.info("Clear cache named findCommentsByArticleId....");
		
		List<String> likers = Utils.parseJson(comment.getLiker());
		String username = SecurityUtils.getUsername();
		if (Utils.existValueInList(username, likers)) {
			likers.remove(username);
		} else {
			if (likers == null)
				likers = new ArrayList<String>();
			likers.add(username);
		}
		commentRepository.updateLiker(comment.getId(), Utils.toJSON(likers));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateState(List<Long> ids) {
		commentRepository.updateState(ids);
	}

}