package com.kracto.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
@RequestMapping({ URL.ADMIN, URL.DASHBOARD })
public class DashboardController {

	@GetMapping
	public String dashboard() {
		return View.ADMIN_HOME;
	}
}
