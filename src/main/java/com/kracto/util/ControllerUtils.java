package com.kracto.util;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.ui.Model;

import com.kracto.domain.Article;
import com.kracto.service.CategoryService;
import com.kracto.web.constant.Keyword;

public final class ControllerUtils {

	public static String reverseType(String type) {
		if (type == null)
			return Keyword.DESC;
		return type.equals(Keyword.ASC) ? Keyword.DESC : Keyword.ASC;
	}

	public static PageRequest setPageRequest(Optional<Integer> pageNumber, int size, String type, String sort) {
		return new PageRequest(setPage(pageNumber), size,
				type.equals(Keyword.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
	}

	private static int setPage(Optional<Integer> pageNumber) {
		if (!pageNumber.isPresent()) {
			return 0;
		}
		if (pageNumber.get() < 1)
			throw new NotFoundException("Current page not found.");
		return pageNumber.get() - 1;

	}

	public static PageRequest setPageRequest(Optional<Integer> pageNumber, int size) {
		return new PageRequest(setPage(pageNumber), size);
	}

	public static void setUserArticlesModel(Model model, Page<Article> articles, CategoryService categoryService,
			String url) {
		model.addAttribute("articles", articles);
		model.addAttribute("categories", categoryService.getByRoot());
		model.addAttribute("url", url);
	}

	public static void setUrlTypeSortModel(Model model, String url, String type, String sort) {
		model.addAttribute("url", url);
		model.addAttribute("type", type);
		model.addAttribute("sort", sort);
	}

}
