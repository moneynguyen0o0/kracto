package com.kracto.web.controller.user;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kracto.data.Role;
import com.kracto.domain.Comment;
import com.kracto.dto.LocalUser;
import com.kracto.dto.UserComment;
import com.kracto.service.CommentService;
import com.kracto.util.SecurityUtils;
import com.kracto.util.Utils;
import com.kracto.web.constant.URL;

@RestController
@RequestMapping(URL.ROOT_CATE + URL.ARTICLE_ID + URL.COMMENTS)
public class CommentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

	private CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping
	public List<UserComment> getComments(@PathVariable Long articleId) {
		LOGGER.info("GET comments at article {} - IP {}", articleId, Utils.getRequestRemoteAddr());

		return commentService.findByArticleId(articleId);
	}

	@PostMapping
	public ResponseEntity<?> addComment(@Valid @RequestBody UserComment userComment, @PathVariable Long articleId) {
		if (!SecurityUtils.isAnonymous()) {
			LOGGER.info("Add comment at article {}- IP {}", articleId, Utils.getRequestRemoteAddr());

			boolean createdByAdmin = SecurityUtils.checkRole(Role.ADMIN);
			Comment comment = new Comment(null, articleId, SecurityUtils.getUsername(), userComment.getContent(),
					((LocalUser) SecurityUtils.getAuthentication().getPrincipal()).getPhoto(), createdByAdmin, null,
					new Date(), null, userComment.getParent(), createdByAdmin);
			commentService.addCommentAndUpdateNumComment(comment);
			userComment.setId(comment.getId());
			return new ResponseEntity<UserComment>(userComment, HttpStatus.OK);
		}

		LOGGER.error("FORBIDDEN - Add comment at article {} - IP {}", articleId, Utils.getRequestRemoteAddr());

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PutMapping(URL.ID)
	public ResponseEntity<?> updateComment(@Valid @RequestBody UserComment userComment, @PathVariable Long id) {
		if (!SecurityUtils.isAnonymous()) {
			Comment comment = commentService.findOne(id);
			if (comment != null) {
				if (comment.getUsername().equals(SecurityUtils.getUsername())) {
					LOGGER.info("Update comment at ID {}, article {}- IP {}", id, comment.getArticleId(),
							Utils.getRequestRemoteAddr());

					comment.setContent(userComment.getContent());
					commentService.updateContent(comment);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			} else {
				LOGGER.error("NOT_FOUND - Update comment at ID {}- IP {}", id, Utils.getRequestRemoteAddr());

				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
		LOGGER.error("FORBIDDEN - Update comment at ID {} - IP {}", id, Utils.getRequestRemoteAddr());

		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@DeleteMapping(URL.ID)
	public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long articleId) {
		if (SecurityUtils.checkRole(Role.ADMIN)) {
			LOGGER.info("Delete comment at ID {}, article {} - IP {}", id, articleId, Utils.getRequestRemoteAddr());

			commentService.deleteIdAndAllParentIds(id, articleId);
			return new ResponseEntity<>(HttpStatus.OK);
		}

		LOGGER.error("FORBIDDEN - Delete comment at ID {} - IP {}", id, Utils.getRequestRemoteAddr());

		return new ResponseEntity<UserComment>(HttpStatus.FORBIDDEN);
	}

	@GetMapping(URL.ID + URL.UPVOTE)
	public ResponseEntity<?> upvoteComment(@PathVariable Long id) {
		if (!SecurityUtils.isAnonymous()) {
			Comment comment = commentService.findOne(id);
			if (comment != null) {
				LOGGER.info("Upvote comment at ID {}, article {} - IP {}", id, comment.getArticleId(),
						Utils.getRequestRemoteAddr());

				commentService.updateLiker(comment);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				LOGGER.error("NOT_FOUND - Upvote comment at ID {}- IP {}", id, Utils.getRequestRemoteAddr());

				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}

		LOGGER.error("FORBIDDEN - Upvote comment at ID {} - IP {}", id, Utils.getRequestRemoteAddr());

		return new ResponseEntity<UserComment>(HttpStatus.FORBIDDEN);
	}

	@PostMapping(URL.UPDATE_STATE)
	public ResponseEntity<?> updateState(@RequestParam(value = "ids[]") List<Long> ids) {
		if (SecurityUtils.checkRole(Role.ADMIN)) {
			LOGGER.info("Update state comments at IDs {} - IP {}", ids, Utils.getRequestRemoteAddr());

			commentService.updateState(ids);
			return new ResponseEntity<>(HttpStatus.OK);
		}

		LOGGER.error("FORBIDDEN - Update state comments - IP {}", Utils.getRequestRemoteAddr());

		return new ResponseEntity<UserComment>(HttpStatus.FORBIDDEN);
	}

}