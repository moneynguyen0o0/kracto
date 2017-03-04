package com.kracto.web.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.kracto.data.Gender;
import com.kracto.data.Offset;
import com.kracto.data.Role;
import com.kracto.domain.Account;
import com.kracto.dto.NewPassword;
import com.kracto.service.AccountService;
import com.kracto.util.ControllerUtils;
import com.kracto.util.SecurityUtils;
import com.kracto.util.Utils;
import com.kracto.validator.AdminAccountValidator;
import com.kracto.validator.FullPasswordValidator;
import com.kracto.validator.SettingAccountValidator;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller("adminAccountController")
public class AccountController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Autowired
	private ServletContext context;

	@Autowired
	private AdminAccountValidator adminAccountValidator;
	@Autowired
	private FullPasswordValidator confirmPasswordValidator;
	@Autowired
	private SettingAccountValidator settingAccountValidator;

	@GetMapping(URL.ADMIN_LOGIN)
	public String login(@RequestParam(value = "error", required = false) String error, Model model, Locale locale) {
		if (SecurityUtils.checkRole(Role.ADMIN) || SecurityUtils.checkRole(Role.SUPERADMIN))
			return "redirect:" + URL.DASHBOARD;
		if (error != null){
			model.addAttribute("isValid", true);
			
			LOGGER.info("ADMIN LOGIN - FAIL - IP {}", Utils.getRequestRemoteAddr());
		}
		return View.ADMIN_LOGIN;
	}

	@GetMapping(value = { URL.ADMIN_ACCOUNTS, URL.ADMIN_ACCOUNTS + URL.PAGING })
	public String list(@PathVariable Optional<Integer> pageNumber,
			@RequestParam(defaultValue = "created", required = false) String sort,
			@RequestParam(defaultValue = Keyword.DESC, required = false) String type, Model model) {
		model.addAttribute("accounts", accountService
				.findAllDESC(ControllerUtils.setPageRequest(pageNumber, Offset.ADMIN_PAGING_SIZE, type, sort)));
		ControllerUtils.setUrlTypeSortModel(model, URL.ADMIN_ACCOUNTS, ControllerUtils.reverseType(type), sort);
		return View.ADMIN_ACCOUNT_LIST;
	}

	@PostMapping(value = { URL.ADMIN_ACCOUNTS + URL.UPDATE_STATE })
	@ResponseStatus(HttpStatus.OK)
	public void updateState(@RequestParam(value = "usernames[]") List<String> usernames) {
		LOGGER.info("ACCOUNT - Update state of usernames {} - ADMIN {}", usernames, SecurityUtils.getUsername());
		accountService.updateState(usernames);
	}

	@GetMapping(value = { URL.ADMIN_ACCOUNTS + URL.SEARCH, URL.ADMIN_ACCOUNTS + URL.SEARCH + URL.PAGING })
	public String search(@PathVariable Optional<Integer> pageNumber, @RequestParam String keyword,
			@RequestParam(defaultValue = "created", required = false) String sort,
			@RequestParam(defaultValue = Keyword.DESC, required = false) String type, Model model) {
		model.addAttribute("accounts", accountService.search(keyword,
				ControllerUtils.setPageRequest(pageNumber, Offset.ADMIN_PAGING_SIZE, type, sort)));
		ControllerUtils.setUrlTypeSortModel(model, URL.ADMIN_ACCOUNTS + URL.SEARCH, ControllerUtils.reverseType(type),
				sort);
		model.addAttribute("keyword", "?keyword=" + keyword + "&");
		return View.ADMIN_ACCOUNT_LIST;
	}

	@GetMapping(URL.ADMIN_ACCOUNTS + URL.DELETE_ID)
	public String delete(@PathVariable("id") String username) {
		Account account = accountService.findOne(username);
		if (account != null){
			accountService.delete(username);
			
			LOGGER.info("ACCOUNT - Delete username {} - ADMIN {}", username, SecurityUtils.getUsername());
		} else { 
			throw new NotFoundException("Account not found");
		}
		return "redirect:" + URL.ADMIN_ACCOUNTS;
	}

	@GetMapping(URL.ADMIN_ACCOUNTS + URL.CREATE)
	public String createAdmin(Model model) {
		setRolesModel(model);
		model.addAttribute("account", new Account());
		return View.ADMIN_ACCOUNT_CREATE;
	}

	private void setRolesModel(Model model) {
		model.addAttribute("roles", Role.getRoles());
	}

	@PostMapping(URL.ADMIN_ACCOUNTS + URL.SAVE)
	public String saveAdmin(@ModelAttribute(value = "account") @Valid Account account, BindingResult result,
			Model model) {
		adminAccountValidator.validate(account, result);
		if (result.hasErrors()) {
			setRolesModel(model);
			return View.ADMIN_ACCOUNT_CREATE;
		}
		account.setPassword(SecurityUtils.encodePassword(account.getPassword()));
		account.setEnabled(true);
		account.setState(true);
		account.setCreated(new Date());
		accountService.save(account);
		return "redirect:" + URL.ADMIN_ACCOUNTS;
	}

	@GetMapping(URL.ADMIN_ACCOUNT + URL.SECURITY)
	public String security(Model model) {
		model.addAttribute("newPassword", new NewPassword());
		return View.ADMIN_ACCOUNT_SECURITY;
	}

	@PostMapping(URL.ADMIN_ACCOUNT + URL.SECURITY)
	public String security(@ModelAttribute(value = "newPassword") @Valid NewPassword newPassword, BindingResult result,
			Model model) {
		confirmPasswordValidator.validate(newPassword, result);
		if (result.hasErrors())
			return View.ADMIN_ACCOUNT_SECURITY;
		accountService.changePassword(newPassword);
		SecurityUtils.logout();
		return "redirect:" + URL.DASHBOARD;
	}

	@GetMapping(URL.ADMIN_ACCOUNT + URL.SETTING)
	public String setting(Model model) {
		Account account = getAccount(SecurityUtils.getUsername());
		viewProfile(model, account);
		setGenderModel(model);
		return View.ADMIN_ACCOUNT_SETTING;
	}

	@PostMapping(URL.ADMIN_ACCOUNT + URL.SETTING)
	public String setting(@RequestParam("photoImage") MultipartFile photoImage,
			@ModelAttribute(value = "account") Account account, BindingResult result, Model model) {
		settingAccountValidator.validate(account, result);
		if (result.hasErrors()) {
			setGenderModel(model);
			return View.ADMIN_ACCOUNT_SETTING;
		}
		Account oldAccount = getAccount(SecurityUtils.getUsername());
		if (oldAccount != null) {
			if (!photoImage.isEmpty()) {
				String root = context.getRealPath("/") + "/resources/images/accounts/";
				// delete old photo
				if (oldAccount.getPhoto() != null) {
					File file = new File(root + oldAccount.getPhoto());
					file.delete();
				}
				// create new photo
				String fileName = SecurityUtils.getUsername() + ".jpg";
				try {
					photoImage.transferTo(new File(root + fileName));
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
				account.setPhoto(fileName);
			}
			accountService.updateMoreInfo(oldAccount, account);
		}
		return "redirect:" + URL.ADMIN_ACCOUNT + URL.PROFILE;
	}

	private void setGenderModel(Model model) {
		model.addAttribute("genders", Gender.getGenders());
	}
	
	@GetMapping(URL.ADMIN_ACCOUNTS + URL.USERNAME + URL.PROFILE)
	public String viewProfile(@PathVariable String username, Model model) {
		Account account = getAccount(username);
		viewProfile(model, account);
		return View.ADMIN_ACCOUNT_PROFILE;
	}

	@GetMapping(URL.ADMIN_ACCOUNT + URL.PROFILE)
	public String viewProfile(Model model) {
		Account account = getAccount(SecurityUtils.getUsername());
		viewProfile(model, account);
		return View.ADMIN_ACCOUNT_PROFILE;
	}
	
	private void viewProfile(Model model, Account account) {
		if (account == null)
			throw new NotFoundException("Account not found");
		model.addAttribute("account", account);
	}

	private Account getAccount(String username) {
		return accountService.findOne(username);
	}

}