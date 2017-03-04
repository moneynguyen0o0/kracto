package com.kracto.web.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kracto.domain.Article;
import com.kracto.domain.Category;
import com.kracto.service.ArticleService;
import com.kracto.service.CategoryService;
import com.kracto.util.SecurityUtils;
import com.kracto.util.Utils;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller("CategoryController")
@RequestMapping(URL.ADMIN_CATEGORIES)
public class CategoryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	private CategoryService categoryService;
	private ArticleService articleService;

	@Autowired
	public CategoryController(CategoryService categoryService, ArticleService articleService) {
		this.categoryService = categoryService;
		this.articleService = articleService;
	}

	@Autowired
	private MessageSource messages;

	@ModelAttribute("category")
	public Category getCategoryLanguage() {
		return new Category();
	}

	@GetMapping
	public String list(@RequestParam(required = false, defaultValue = "0") Integer rootId, Model model) {
		model.addAttribute("categories", categoryService.findByRootAndParent(rootId));
		model.addAttribute("rootId", rootId);
		return View.ADMIN_CATEGORY;
	}

	@GetMapping(URL.DELETE)
	public String delete(@RequestParam Integer id, @RequestParam Integer rootId, RedirectAttributes attr, Locale locale) {
		Category category = null;
		if (rootId == 0) {
			if (categoryService.countSub(id) > 0) {
				attr.addFlashAttribute("message",
						messages.getMessage("admin.category.delete.subcategories", null, locale));
			} else {
				category = findCategory(id);
				if (category != null) {
					if (articleService.existCate(category.getNameEn())) {
						attr.addFlashAttribute("message",
								messages.getMessage("admin.category.delete.articles", null, locale));
					} else {
						delete(id);

						LOGGER.info("ADMIN {} - Deleted category with ID {}", SecurityUtils.getUsername(), id);
					}
				}
			}
			return "redirect:" + URL.ADMIN_CATEGORIES;
		} else {
			category = findCategory(id);
			Category rootCate = findCategory(rootId);
			if (category != null && rootCate != null) {
				if (articleService.existCate(rootCate.getNameEn(), Utils.toJSON(category.getNameEn()))) {
					attr.addFlashAttribute("message",
							messages.getMessage("admin.category.delete.articles", null, locale));
				} else {
					delete(id);

					LOGGER.info("ADMIN {} - Deleted category with ID {}", SecurityUtils.getUsername(), id);
				}
			}
			return "redirect:" + URL.ADMIN_CATEGORIES + "?rootId=" + rootId;
		}
	}

	private Category findCategory(Integer id) {
		return categoryService.findOne(id);
	}

	private void delete(Integer id) {
		try {
			categoryService.delete(id);
			categoryService.clearCategoriesByRootMapCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(URL.SAVE)
	public String save(@ModelAttribute(value = "category") @Valid Category category, BindingResult result,
			RedirectAttributes attr, HttpServletRequest request) {
		String action = request.getParameter("action");
		boolean flag = false;
		if (result.hasErrors()) {
			flag = true;
		} else {
			try {
				if (action.equals(Keyword.CREATE)) {
					categoryService.save(category);
				} else if (action.equals(Keyword.UPDATE)) {
					Category foundCate = findCategory(category.getId());
					if (foundCate != null) {
						if (!foundCate.getNameEn().equals(category.getNameEn())) {
							if (foundCate.getParent() == 0) {
								List<Article> articles = articleService.findByRootCate(foundCate.getNameEn());
								for (Article article : articles) {
									article.setRootCate(category.getNameEn());
									article.setUpdated(new Date());
								}
								articleService.update(articles);
							} else {
								Category foundRootCate = findCategory(foundCate.getParent());
								if (foundRootCate != null) {
									List<Article> articles = articleService.findBySubCate(foundRootCate.getNameEn(),
											Utils.toJSON(foundCate.getNameEn()));
									for (Article article : articles) {
										article.setSubCate(article.getSubCate().replace(foundCate.getNameEn(),
												category.getNameEn()));
										article.setUpdated(new Date());
									}
									articleService.update(articles);
								}
							}
						}
						
						foundCate.setNameEn(category.getNameEn());
						foundCate.setNameVi(category.getNameVi());
						categoryService.update(foundCate);
						
						LOGGER.info("ADMIN {} - Updated category with ID {}", SecurityUtils.getUsername(),
								foundCate.getId());
					} else {
						throw new NotFoundException("Category not found");
					}
				}
				
				categoryService.clearCategoriesByRootMapCache();
			} catch (DuplicateKeyException e) {
				result.rejectValue("id", "errors.existing");
				flag = true;
			}
		}
		if (flag) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
			attr.addFlashAttribute("category", category);
			attr.addFlashAttribute("action", action);
		}
		if (category.getParent() == 0)
			return "redirect:" + URL.ADMIN_CATEGORIES;
		return "redirect:" + URL.ADMIN_CATEGORIES + "?rootId=" + category.getParent();
	}

}