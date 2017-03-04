package com.kracto.web.controller.user;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.Environment;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kracto.data.Gender;
import com.kracto.data.Role;
import com.kracto.domain.Account;
import com.kracto.dto.NewPassword;
import com.kracto.dto.Password;
import com.kracto.service.AccountService;
import com.kracto.service.EmailService;
import com.kracto.util.SecurityUtils;
import com.kracto.util.Utils;
import com.kracto.validator.AccountValidator;
import com.kracto.validator.ConfirmPasswordValidator;
import com.kracto.validator.FullPasswordValidator;
import com.kracto.validator.SettingAccountValidator;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	private static final String IMAMGE_ROOT_URL = "/resources/images/accounts/";

	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	private EmailService emailService;

	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;
	@Autowired
	private MessageSource messages;
	@Autowired
	private AccountValidator accountValidator;
	@Autowired
	private ConfirmPasswordValidator confirmPasswordValidator;
	@Autowired
	private FullPasswordValidator fullPasswordValidator;
	@Autowired
	private SettingAccountValidator settingAccountValidator;

	@GetMapping(URL.SIGNIN)
	public String signin(@RequestParam(value = "error", required = false) String error, Model model) {
		LOGGER.info("Signin page is visited - IP {}", Utils.getRequestRemoteAddr());
		if (!SecurityUtils.isAnonymous())
			return "redirect:" + URL.WELCOME;
		if (error != null) {
			LOGGER.error("Login fail - IP {}", Utils.getRequestRemoteAddr());

			model.addAttribute("isValid", true);
		}
		return View.SIGNIN;
	}

	@GetMapping(URL.SIGNUP)
	public String signup(Model model) {
		LOGGER.info("Signup page is visited - IP {}", Utils.getRequestRemoteAddr());
		if (!SecurityUtils.isAnonymous())
			return "redirect:" + URL.WELCOME;
		model.addAttribute("account", new Account());
		return View.SIGNUP;
	}

	@PostMapping(URL.ACCOUNT + URL.SAVE)
	public String save(@ModelAttribute(value = "account") @Valid Account account, BindingResult result, Locale locale,
			Model model) throws NoSuchMessageException, MessagingException {
		accountValidator.validate(account, result);
		if (result.hasErrors()) {
			LOGGER.error("Save account - IP {}", Utils.getRequestRemoteAddr());

			return View.SIGNUP;
		}

		LOGGER.info("Save account {} - IP {}", account.getEmail(), Utils.getRequestRemoteAddr());

		String token = Utils.randomToken();
		account.setPassword(encodePassword(account.getPassword()));
		account.setRole(Role.MEMBER);
		account.setEnabled(false);
		account.setState(false);
		account.setCreated(new Date());
		account.setToken(token);
		accountService.save(account);

		String url = getUrl() + URL.VERIFY_EMAIL + URL.SLASH + token;
		emailService.sendForVerifyAccount(url, account.getEmail(),
				messages.getMessage("user.signup.conformation.subject", null, locale), Utils.getLocale());

		model.addAttribute("message", messages.getMessage("user.signup.conformation.message", null, locale));

		return View.USER_MESSAGE;
	}

	@GetMapping(URL.VERIFY_EMAIL_TOKEN)
	public String verifyAccount(@PathVariable String token) {
		LOGGER.info("Verify account with token {} - IP {}", token, Utils.getRequestRemoteAddr());

		Account account = accountService.findUnEnabledByToken(token);
		if (account == null)
			throw new NotFoundException("Account not found");
		account.setEnabled(true);
		account.setToken(Utils.randomToken());
		accountService.update(account);

		return "redirect:" + URL.ACCOUNT_PROFILE;
	}

	@GetMapping(URL.RESET_PASSWORD)
	public String resetPassword() {
		LOGGER.info("Reset password page is visited - IP {}", Utils.getRequestRemoteAddr());

		return View.USER_RESET_PASSWORD;
	}

	@PostMapping(URL.RESET_PASSWORD)
	public String resetPassword(@RequestParam String email, Locale locale, Model model)
			throws NoSuchMessageException, MessagingException {
		LOGGER.info("Reset password with email {} - IP {}", email, Utils.getRequestRemoteAddr());

		Account account = accountService.findEnabledByEmail(email);
		if (account == null) {
			LOGGER.error("Reset password email {} not found - IP {}", email, Utils.getRequestRemoteAddr());

			model.addAttribute("isNotFound", true);

			return View.USER_RESET_PASSWORD;
		}
		String token = Utils.randomToken();
		account.setToken(token);
		accountService.update(account);
		String url = getUrl() + URL.RESET_PASSWORD + URL.SLASH + token;
		emailService.sendForResetPassword(url, account.getEmail(),
				messages.getMessage("user.resetpassword.subject", null, locale), Utils.getLocale());

		model.addAttribute("message", messages.getMessage("user.resetpassword.message", null, locale));

		return View.USER_MESSAGE;
	}

	@GetMapping(URL.RESET_PASSWORD_TOKEN)
	public String changePassword(@PathVariable String token, Model model) {
		LOGGER.info("GET - Reset password with token {} - IP {}", token, Utils.getRequestRemoteAddr());

		Account account = accountService.findByToken(token);
		if (account == null)
			throw new NotFoundException("Account not found");
		model.addAttribute("password", new Password());
		setTokenModel(token, model);

		return View.USER_CHANGE_PASSWORD;
	}

	@PostMapping(URL.CHANGE_PASSWORD_TOKEN)
	public String changePassword(@PathVariable String token,
			@ModelAttribute(value = "password") @Valid Password password, BindingResult result, Locale locale,
			Model model) {
		LOGGER.info("POST - Reset password with token {} - IP {}", token, Utils.getRequestRemoteAddr());

		confirmPasswordValidator.validate(password, result);
		if (result.hasErrors()) {
			LOGGER.error("POST - Reset password with token {} - IP {}", token, Utils.getRequestRemoteAddr());

			setTokenModel(token, model);

			return View.USER_CHANGE_PASSWORD;
		}

		Account account = accountService.findByToken(token);
		if (account == null)
			throw new NotFoundException("Account not found");
		account.setPassword(encodePassword(password.getNewPassword()));
		account.setToken(Utils.randomToken());
		accountService.update(account);

		model.addAttribute("message", messages.getMessage("user.resetpassword.success", null, locale));

		return View.USER_MESSAGE;
	}

	private void setTokenModel(String token, Model model) {
		model.addAttribute("token", token);
	}

	private String encodePassword(String password) {
		return SecurityUtils.encodePassword(password);
	}

	@GetMapping(URL.ACCOUNT_PROFILE)
	public String viewProfile(Model model) {
		LOGGER.info("View profile with username {} - IP {}", SecurityUtils.getUsername(), Utils.getRequestRemoteAddr());

		setAccountModel(model);
		setNewPasswordModel(model);
		setGenderModel(model);

		return View.USER_ACCOUNT_PROFILE;
	}

	@PostMapping(URL.ACCOUNT_PROFILE)
	public String updateProfile(@RequestParam("photoImage") MultipartFile photoImage,
			@ModelAttribute(value = "account") Account account, BindingResult result, Model model) {
		LOGGER.info("Change profile with username {} - IP {}", SecurityUtils.getUsername(),
				Utils.getRequestRemoteAddr());

		settingAccountValidator.validate(account, result);
		if (result.hasErrors()) {
			LOGGER.error("Change profile with username {} - IP {}", SecurityUtils.getUsername(),
					Utils.getRequestRemoteAddr());

			setNewPasswordModel(model);
			setGenderModel(model);

			return View.USER_ACCOUNT_PROFILE;
		}

		Account oldAccount = getAccount();
		if (oldAccount != null) {
			if (!photoImage.isEmpty()) {
				String root = context.getRealPath("/") + IMAMGE_ROOT_URL;
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

		return "redirect:" + URL.ACCOUNT_PROFILE;
	}

	@PostMapping(URL.ACCOUNT_CHANGE_PASSWORD)
	public String changePassword(@ModelAttribute(value = "newPassword") @Valid NewPassword newPassword,
			BindingResult result, Model model, Locale locale) {
		LOGGER.info("Change password with username {} - IP {}", SecurityUtils.getUsername(),
				Utils.getRequestRemoteAddr());

		fullPasswordValidator.validate(newPassword, result);
		if (result.hasErrors()) {
			LOGGER.error("Change password with username {} - IP {}", SecurityUtils.getUsername(),
					Utils.getRequestRemoteAddr());

			setAccountModel(model);
			setGenderModel(model);

			return View.USER_ACCOUNT_PROFILE;
		}

		accountService.changePassword(newPassword);
		
		SecurityUtils.logout();
		
		model.addAttribute("message", messages.getMessage("user.changepassword.message", null, locale)
				+ "<script>setTimeout(function () {window.location.href = '" + getUrl() + URL.SIGNIN + "';}, 3000);</script>");

		return View.USER_MESSAGE;
	}

	private Account getAccount() {
		return accountService.findOne(SecurityUtils.getUsername());
	}

	private void setAccountModel(Model model) {
		Account account = getAccount();
		if (account == null)
			throw new NotFoundException("Account not found.");
		model.addAttribute("account", account);
	}

	private void setNewPasswordModel(Model model) {
		model.addAttribute("newPassword", new NewPassword());
	}

	private void setGenderModel(Model model) {
		model.addAttribute("genders", Gender.getGenders());
	}

	private String getUrl() {
		return env.getProperty("root.url") + context.getContextPath();
	}

}
