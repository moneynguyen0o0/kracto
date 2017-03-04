package com.kracto.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kracto.service.AccountService;
import com.kracto.service.CommentService;

@ControllerAdvice(basePackages = "com.kracto.web.controller.admin")
public class AppWideAdminController {

	private AccountService accountService;
	private CommentService commentService;

	@Autowired
	public AppWideAdminController(AccountService accountService, CommentService commentService) {
		this.accountService = accountService;
		this.commentService = commentService;
	}

	@ModelAttribute("countNewAccounts")
	public Long countNewAccounts() {
		return accountService.countNewState();
	}

	@ModelAttribute("countNewComments")
	public Long countNewComments() {
		return commentService.countNewState();
	}

}
