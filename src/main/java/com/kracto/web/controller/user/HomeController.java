package com.kracto.web.controller.user;

import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.kracto.data.Offset;
import com.kracto.service.ArticleService;
import com.kracto.service.CategoryService;
import com.kracto.util.ControllerUtils;
import com.kracto.util.Utils;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	private ArticleService articleService;
	private CategoryService categoryService;

	@Autowired
	public HomeController(ArticleService articleService, CategoryService categoryService) {
		this.articleService = articleService;
		this.categoryService = categoryService;
	}
	
	@Autowired
	private MessageSource messages;

	@GetMapping(value = { URL.SLASH, URL.WELCOME, URL.PAGING })
	public String home(@PathVariable Optional<Integer> pageNumber, Model model, Locale locale) {
		LOGGER.info("Home page is visited at page {} - IP {}", pageNumber.isPresent() ? pageNumber.get() : 0,
				Utils.getRequestRemoteAddr());
		
		ControllerUtils.setUserArticlesModel(model,
				articleService.getTopOnUser(ControllerUtils.setPageRequest(pageNumber, Offset.USER_PAGING_SIZE)),
				categoryService, null);
		
		model.addAttribute("dynamicTitle", messages.getMessage("tilte.home", null, locale));
		
		return View.USER_LIST;
	}

	@GetMapping(value = { URL.SEARCH, URL.SEARCH + URL.PAGING })
	public String search(@PathVariable Optional<Integer> pageNumber, @RequestParam String keyword,
			@RequestParam(defaultValue = Keyword.ALL, required = false) String type, Model model, Locale locale) {
		LOGGER.info("Search page is visited at page {}, keyword {}, type {} - IP {}",
				pageNumber.isPresent() ? pageNumber.get() : 0, keyword, type, Utils.getRequestRemoteAddr());
		
		PageRequest pageRequest = ControllerUtils.setPageRequest(pageNumber, Offset.USER_PAGING_SIZE);
		ControllerUtils
				.setUserArticlesModel(model,
						type.equals(Keyword.ALL) ? articleService.searchWithProc(keyword, pageRequest)
								: articleService.searchWithProc(keyword, type, pageRequest),
						categoryService, URL.SEARCH);
		model.addAttribute("type", type);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("dynamicTitle",messages.getMessage("title.search", null, locale) + " " + keyword);
		
		return View.USER_SEARCH;
	}

	@GetMapping(URL.ABOUT)
	public String about(Model model, Locale locale) {
		LOGGER.info("About page is visited - IP {}",
				Utils.getRequestRemoteAddr());
		
		model.addAttribute("title", messages.getMessage("user.about.title", null, locale));
		model.addAttribute("content", messages.getMessage("pages.about", null, locale));
		
		return View.ABOUT;
	}
	
	@GetMapping(URL.GUIDE)
	public String guide(Model model, Locale locale) {
		LOGGER.info("Guide page is visited - IP {}",
				Utils.getRequestRemoteAddr());
		
		model.addAttribute("title", messages.getMessage("user.guide.title", null, locale));
		model.addAttribute("content", messages.getMessage("pages.guide", null, locale));
		
		return View.GUIDE;
	}

}