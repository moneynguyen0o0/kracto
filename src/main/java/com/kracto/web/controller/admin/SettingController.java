package com.kracto.web.controller.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
@RequestMapping(URL.ADMIN_SETTING)
public class SettingController {

	@Autowired
	private ServletContext context;

	@GetMapping(URL.UTILPAGE)
	public String loadUtilPage(@RequestParam(required = false, defaultValue = Keyword.VI) String lang, Model model) {
		model.addAttribute("messages", getMessages(getPageResource(lang)));
		model.addAttribute("lang", lang);
		return View.ADMIN_UTILPAGE;
	}

	@PostMapping(URL.UTILPAGE)
	public String saveUtilPage(@RequestParam(required = false, defaultValue = Keyword.VI) String lang,
			HttpServletRequest request, Model model) {
		saveMessages(getPageResource(lang), request);
		model.addAttribute("lang", lang);
		return "redirect:" + URL.ADMIN_SETTING + URL.UTILPAGE;
	}

	@GetMapping(URL.I18N)
	public String loadI18n(@PathVariable String name,
			@RequestParam(required = false, defaultValue = Keyword.VI) String lang, Model model) {
		model.addAttribute("messages", getMessages(getI18nResource(name + "_" + lang)));
		model.addAttribute("name", name);
		model.addAttribute("lang", lang);
		return View.ADMIN_I18N;
	}

	@PostMapping(URL.I18N + URL.SAVE)
	public String saveI18n(@PathVariable String name,
			@RequestParam(required = false, defaultValue = Keyword.VI) String lang, HttpServletRequest request,
			Model model) {
		saveMessages(getI18nResource(name + "_" + lang), request);
		model.addAttribute("name", name);
		model.addAttribute("lang", lang);
		return "redirect:" + URL.ADMIN_SETTING + URL.I18N;
	}

	@GetMapping(URL.CONFIG)
	public String loadConfig(@PathVariable String name, Model model) {
		model.addAttribute("messages", getMessages(getConfigResource(name)));
		model.addAttribute("name", name);
		return View.ADMIN_CONFIG;
	}

	@PostMapping(URL.CONFIG + URL.SAVE)
	public String saveCofig(@PathVariable String name, HttpServletRequest request, Model model) {
		saveMessages(getConfigResource(name), request);
		model.addAttribute("name", name);
		return "redirect:" + URL.ADMIN_SETTING + URL.CONFIG;
	}

	@GetMapping(URL.MENU)
	public String loadMenu(@RequestParam(required = false, defaultValue = Keyword.VI) String lang, Model model) {
		Resource resource = getMenuResource(lang);
		Properties prop = loadResource(resource);
		model.addAttribute("menu", prop.getProperty("menu.admin"));
		model.addAttribute("lang", lang);
		return View.ADMIN_MENU;
	}

	@PostMapping(URL.MENU)
	public String saveMenu(HttpServletRequest request, Model model) {
		String menuUser = request.getParameter("menu-user");
		String menuAdmin = request.getParameter("menu-admin");
		String lang = request.getParameter("lang");
		Resource resource = getMenuResource(lang);
		Properties prop = loadResource(resource);
		prop.replace("menu.user", menuUser);
		prop.replace("menu.admin", menuAdmin);
		storeResource(prop, resource);
		return "redirect:" + URL.ADMIN_SETTING + URL.MENU + "?lang=" + lang;
	}

	@GetMapping(URL.BANNER)
	public String loadBanner(@RequestParam(required = false, defaultValue = Keyword.VI) String lang, Model model) {
		Resource resource = getBannerResource(lang);
		Properties prop = loadResource(resource);
		model.addAttribute("title", prop.getProperty("banner.title"));
		model.addAttribute("description", prop.getProperty("banner.description"));
		model.addAttribute("lang", lang);
		return View.ADMIN_BANNER;
	}

	@PostMapping(URL.BANNER)
	public String saveBanner(@RequestParam("bannerImage") MultipartFile bannerImage, HttpServletRequest request,
			Model model) {
		String menuUser = request.getParameter("title");
		String menuAdmin = request.getParameter("description");
		String lang = request.getParameter("lang");
		if (!bannerImage.isEmpty()) {
			String pathFile = context.getRealPath("/") + "/resources/images/bg.jpg";
			// delete old banner
			(new File(pathFile)).delete();
			// create new banner
			try {
				bannerImage.transferTo(new File(pathFile));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		Resource resource = getBannerResource(lang);
		Properties prop = loadResource(resource);
		prop.replace("banner.title", menuUser);
		prop.replace("banner.description", menuAdmin);
		storeResource(prop, resource);
		return "redirect:" + URL.ADMIN_SETTING + URL.BANNER + "?lang=" + lang;
	}

	private void saveMessages(Resource resource, HttpServletRequest request) {
		Properties prop = new Properties();
		for (Map.Entry<String, String> entry : getMessages(resource).entrySet()) {
			String value = request.getParameter(entry.getKey());
			if (value != null)
				prop.setProperty(entry.getKey(), value);
		}
		storeResource(prop, resource);
	}

	private Map<String, String> getMessages(Resource resource) {
		Properties prop = loadResource(resource);
		Map<String, String> result = new HashMap<String, String>();
		if (prop == null)
			throw new NotFoundException("Resource not found");
		for (Object o : prop.keySet()) {
			String key = (String) o;
			result.put(key, prop.getProperty(key));
		}
		return result;
	}

	private Resource getPageResource(String lang) {
		return getI18nResource("page" + "_" + lang);
	}

	private Resource getBannerResource(String lang) {
		return getI18nResource("banner" + "_" + lang);
	}

	private Resource getMenuResource(String lang) {
		return getI18nResource("menu" + "_" + lang);
	}

	private Resource getConfigResource(String name) {
		return new ClassPathResource("/" + name + ".properties");
	}

	private Resource getI18nResource(String name) {
		return new ClassPathResource("/i18n/" + name + ".properties");
	}

	private Properties loadResource(Resource resource) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(resource.getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private void storeResource(Properties prop, Resource resource) {
		try {
			prop.store(new FileOutputStream(resource.getFile()), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}