package com.kracto.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kracto.web.constant.URL;

@Controller
public class GlobalErrorController {

	@RequestMapping(value = URL.ERROR_403)
	public String error403() {
		return "error.403";
	}

	@RequestMapping(value = URL.ERROR_404)
	public String error404() {
		return "error.404";
	}

	@RequestMapping(value = URL.ERROR_500)
	public String error500() {
		return "error.500";
	}
}
