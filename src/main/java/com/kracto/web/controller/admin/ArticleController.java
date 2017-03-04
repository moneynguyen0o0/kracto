package com.kracto.web.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kracto.data.Offset;
import com.kracto.domain.Article;
import com.kracto.domain.Category;
import com.kracto.service.ArticleService;
import com.kracto.service.CategoryService;
import com.kracto.util.ControllerUtils;
import com.kracto.util.SecurityUtils;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller("adminArticleController")
@RequestMapping(URL.ADMIN_ARTICLES)
public class ArticleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

	private CategoryService categoryService;
	private ArticleService articleService;

	@Autowired
	public ArticleController(ArticleService articleService, CategoryService categoryService) {
		this.articleService = articleService;
		this.categoryService = categoryService;
	}

	@GetMapping(value = { URL.EMPTY, URL.SLASH, URL.PAGING })
	public String list(@PathVariable Optional<Integer> pageNumber,
			@RequestParam(defaultValue = "id", required = false) String sort,
			@RequestParam(defaultValue = Keyword.DESC, required = false) String type, Model model) {
		model.addAttribute("articles", articleService
				.getTopOnAdmin(ControllerUtils.setPageRequest(pageNumber, Offset.ADMIN_PAGING_SIZE, type, sort)));
		ControllerUtils.setUrlTypeSortModel(model, URL.ADMIN_ARTICLES, ControllerUtils.reverseType(type), sort);
		return View.ADMIN_ARTICLE_LIST;
	}

	@GetMapping(value = { URL.SEARCH, URL.SEARCH + URL.PAGING })
	public String search(@PathVariable Optional<Integer> pageNumber, @RequestParam String keyword,
			@RequestParam(defaultValue = "id", required = false) String sort,
			@RequestParam(defaultValue = Keyword.DESC, required = false) String type, Model model) {
		model.addAttribute("articles", articleService.search(keyword,
				ControllerUtils.setPageRequest(pageNumber, Offset.ADMIN_PAGING_SIZE, type, sort)));
		ControllerUtils.setUrlTypeSortModel(model, URL.ADMIN_ARTICLES + URL.SEARCH, ControllerUtils.reverseType(type),
				sort);
		model.addAttribute("keyword", "?keyword=" + keyword + "&");
		return View.ADMIN_ARTICLE_LIST;
	}

	@GetMapping(URL.DELETE_ID)
	public String delete(@PathVariable Long id, RedirectAttributes attr) {
		try {
			Article article = findArticle(id);
			if (article == null)
				throw new NotFoundException("Article not found");
			if (!article.getUsername().equals(SecurityUtils.getUsername()))
				return "redirect:" + URL.ERROR_403;
			articleService.delete(article);

			LOGGER.info("ADMIN {} - Deleted article with ID {}", SecurityUtils.getUsername(), id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + URL.ADMIN_ARTICLES;
	}

	@GetMapping(URL.ID)
	public String find(@PathVariable Long id) {
		Article article = findArticle(id);
		if (article == null)
			throw new NotFoundException("Article not found");
		return "redirect:" + URL.SLASH + article.getRootCate() + URL.SLASH + article.getId();
	}

	@GetMapping(URL.CREATE)
	public String create(Model model) {
		addCategoriesModel(model);
		model.addAttribute("article", new Article());
		return View.ADMIN_ARTICLE_CRUP;
	}

	@GetMapping(URL.EDIT_ID)
	public String edit(@PathVariable Long id, Model model) {
		addCategoriesModel(model);
		Article article = findArticle(id);
		if (article == null)
			throw new NotFoundException("Article not found");
		if (!article.getUsername().equals(SecurityUtils.getUsername()))
			return "redirect:" + URL.ERROR_403;
		model.addAttribute("article", article);
		return View.ADMIN_ARTICLE_CRUP;
	}

	@PostMapping({URL.SAVE, URL.PREVIEW})
	public String save(@RequestParam(required = false) Boolean preview,
			@ModelAttribute(value = "article") @Valid Article article, BindingResult result, Model model) {
		boolean flag = false;
		if (result.hasErrors()) {
			flag = true;
		} else {
			Category category = categoryService.findOne(Integer.parseInt(article.getRootCate()));
			if (category != null) {
				article.setRootCate(category.getNameEn());
				List<Integer> ids = Stream.of(article.getSubCate().split(",")).map(Integer::parseInt)
						.collect(Collectors.toList());
				List<Category> categories = categoryService.findAll(ids);
				if (categories.size() != ids.size()) {
					result.rejectValue("subCate", "errors.existing.none");
					flag = true;
				} else {
					JSONArray subCates = new JSONArray();
					for (Category subCate : categories)
						if (subCate.getParent() == category.getId())
							subCates.put(subCate.getNameEn());
					article.setSubCate(subCates.toString());
				}
			} else {
				result.rejectValue("rootCate", "errors.existing.none");
				flag = true;
			}
		}
		
		if (flag) {
			addCategoriesModel(model);
			return View.ADMIN_ARTICLE_CRUP;
		}
		
		article.setUsername(SecurityUtils.getUsername());
		article.setCreated(new Date());
		article.setComment(0);
		article.setViewer(0);
		
		if (preview) {
			model.addAttribute("article", article);
			return View.ADMIN_ARTICLE_SINGLE;
		}
		
		if (article.getId() == null) {
			 articleService.save(article);
		} else {
			Article foundArticle = findArticle(article.getId());
			if (foundArticle == null)
				throw new NotFoundException("Article not found");
			article.setCreated(foundArticle.getCreated());
			article.setUsername(foundArticle.getUsername());
			article.setComment(foundArticle.getComment());
			article.setViewer(foundArticle.getViewer());
			article.setUpdated(new Date());
			articleService.update(article);

			LOGGER.info("ADMIN {} - Updated article with ID {}", SecurityUtils.getUsername(), article.getId());
		}
		return "redirect:" + URL.ADMIN_ARTICLES;
	}

	private void addCategoriesModel(Model model) {
		model.addAttribute("categories", categoryService.findAll());
	}

	private Article findArticle(Long id) {
		return articleService.findOne(id);
	}

}