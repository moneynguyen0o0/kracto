package com.kracto.web.controller.user;

import java.util.Optional;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kracto.data.Offset;
import com.kracto.domain.Article;
import com.kracto.dto.ViewCounter;
import com.kracto.service.ArticleService;
import com.kracto.service.CategoryService;
import com.kracto.util.ControllerUtils;
import com.kracto.util.Utils;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
@RequestMapping(URL.ROOT_CATE)
public class ArticleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

	/**
	 * If same scoped beans are wired together there's no problem. For example a
	 * singleton bean A injected into another singleton bean B. But if the bean
	 * A has the narrower scope say prototype scope then there's a problem.
	 */
	@Autowired
	private Provider<ViewCounter> viewCounterProvider;

	private ArticleService articleService;
	private CategoryService categoryService;

	@Autowired
	public ArticleController(ArticleService articleService, CategoryService categoryService) {
		this.articleService = articleService;
		this.categoryService = categoryService;
	}

	@GetMapping(value = { URL.EMPTY, URL.SLASH, URL.PAGING })
	public String list(@PathVariable String rootCate, @PathVariable Optional<Integer> pageNumber, Model model) {
		LOGGER.info("{} page is visited at page {} - IP {}", rootCate, pageNumber.isPresent() ? pageNumber.get() : 0,
				Utils.getRequestRemoteAddr());

		Page<Article> page = articleService.findByCateWithProc(rootCate,
				ControllerUtils.setPageRequest(pageNumber, Offset.USER_PAGING_SIZE));
		if (checkArticleNotFound(page))
			throw new NotFoundException("Article not found");
		ControllerUtils.setUserArticlesModel(model, page, categoryService, URL.SLASH + rootCate);
		
		model.addAttribute("dynamicTitle", rootCate);

		return View.USER_LIST;
	}

	@GetMapping(value = { URL.SUB_CATE, URL.SUB_CATE + URL.PAGING })
	public String listSub(@PathVariable String rootCate, @PathVariable String subCate,
			@PathVariable Optional<Integer> pageNumber, Model model) {
		if (NumberUtils.isNumber(subCate)) {
			LOGGER.info("{} / {} page is visited - IP {}", rootCate, subCate, Utils.getRequestRemoteAddr());

			Article article = articleService.findByIdWithProc(Long.parseLong(subCate));
			if (article == null)
				throw new NotFoundException("Article not found");
			model.addAttribute("article", article);
			model.addAttribute("tags", Utils.parseJson(article.getSubCate()));
			model.addAttribute("rootCate", rootCate);
			
			model.addAttribute("dynamicTitle", Utils.isVi() ? article.getTitleVi() : article.getTitleEn());

			return View.USER_SINGLE;
		} else {
			LOGGER.info("{} / {} page is visited at page {} - IP {}", rootCate, subCate,
					pageNumber.isPresent() ? pageNumber.get() : 0, Utils.getRequestRemoteAddr());

			Page<Article> page = articleService.findByCateWithProc(rootCate, Utils.toJSON(subCate),
					ControllerUtils.setPageRequest(pageNumber, Offset.USER_PAGING_SIZE));
			if (checkArticleNotFound(page))
				throw new NotFoundException("Article not found");
			ControllerUtils.setUserArticlesModel(model, page, categoryService,
					URL.SLASH + rootCate + URL.SLASH + subCate);
			
			model.addAttribute("dynamicTitle", subCate);

			return View.USER_LIST;
		}
	}

	@PostMapping(URL.ID)
	public String getRelatedPosts(@PathVariable String rootCate, @PathVariable Long id, HttpServletRequest request,
			Model model) {
		LOGGER.info("Get related posts at article {} / {} - IP {}", rootCate, id, Utils.getRequestRemoteAddr());
		
		model.addAttribute("articles",
				articleService.findRelatedWithProc(id, rootCate, request.getParameter("subCate")));
		model.addAttribute("categories", categoryService.getByRoot());

		return View.USER_RELATED_ARTICLE;
	}

	@PostMapping(URL.ID + URL.UPDATE_VIEWER)
	public ResponseEntity<?> updateViewer(@PathVariable Long id) {
		LOGGER.info("Update viewer at article {} - IP {}", id, Utils.getRequestRemoteAddr());

		if (!viewCounterProvider.get().isExist(id)) {
			viewCounterProvider.get().add(id);
			articleService.updateViewer(id);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private boolean checkArticleNotFound(Page<Article> page) {
		return page.getContent().size() == 0;
	}

}