package com.kracto.web.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kracto.data.Offset;
import com.kracto.service.CommentService;
import com.kracto.util.ControllerUtils;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller("adminCommentController")
@RequestMapping(URL.ADMIN_COMMENTS)
public class CommentController {

	private CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping(value = { URL.EMPTY, URL.SLASH, URL.PAGING })
	public String list(@PathVariable Optional<Integer> pageNumber,
			@RequestParam(defaultValue = "id", required = false) String sort,
			@RequestParam(defaultValue = Keyword.DESC, required = false) String type, Model model) {
		model.addAttribute("comments", commentService
				.findAllOnAdmin(ControllerUtils.setPageRequest(pageNumber, Offset.ADMIN_PAGING_SIZE, type, sort)));
		ControllerUtils.setUrlTypeSortModel(model, URL.ADMIN_COMMENTS, ControllerUtils.reverseType(type), sort);
		return View.ADMIN_COMMENT;
	}

}