package com.kracto.web.constant;

public final class URL {

	public static final String EMPTY = "";
	public static final String SLASH = "/";
	public static final String PAGING = "/page/{pageNumber}";
	public static final String SEARCH = "/search";
	public static final String ERROR_403 = "/403";
	public static final String ERROR_404 = "/404";
	public static final String ERROR_500 = "/500";

	public static final String ADD = "/add";
	public static final String CREATE = "/create";
	public static final String SAVE = "/save";
	public static final String EDIT_ID = "/edit/{id}";
	public static final String UPDATE = "/update";
	public static final String DELETE = "/delete";
	public static final String DELETE_ID = "/delete/{id}";

	public static final String UPDATE_STATE = "/update-state";
	public static final String SECURITY = "/security";
	public static final String PROFILE = "/profile";
	public static final String SETTING = "/setting";
	
	public static final String IMAGES = "/images";
	public static final String CSS = "/css";
	public static final String JS = "/js";
	public static final String FONTS = "/fonts";
	public static final String CKEDITOR = "/ckeditor";

	/**
	 * USER
	 */
	public static final String WELCOME = "/welcome";
	public static final String SIGNIN = "/signin";
	public static final String LOGOUT = "/logout";
	public static final String SIGNUP = "/signup";
	public static final String TOKEN = "/{token}";
	public static final String ACCOUNT = "/account";
	public static final String USERNAME = "/{username}";
	public static final String VERIFY_EMAIL = "/verify-email";
	public static final String VERIFY_EMAIL_TOKEN = VERIFY_EMAIL + TOKEN;
	public static final String RESET_PASSWORD = "/reset-password";
	public static final String RESET_PASSWORD_TOKEN = RESET_PASSWORD + TOKEN;
	public static final String CHANGE_PASSWORD = "/change-password";
	public static final String CHANGE_PASSWORD_TOKEN = CHANGE_PASSWORD + TOKEN;
	public static final String ACCOUNT_PROFILE = ACCOUNT + PROFILE;
	public static final String ACCOUNT_CHANGE_PASSWORD = ACCOUNT + CHANGE_PASSWORD;
	public static final String ROOT_CATE = "/{rootCate}";
	public static final String SUB_CATE = "/{subCate}";
	public static final String ID = "/{id}";
	public static final String ARTICLE_ID = "/{articleId}";
	public static final String COMMENTS = "/comments";
	public static final String UPVOTE = "/upvotes";
	public static final String UPDATE_VIEWER = "/update-viewer";
	public static final String ABOUT = "/about";
	public static final String GUIDE = "/guide";

	/**
	 * ADMIN
	 */
	
	public static final String PREVIEW = "/preview";

	public static final String NAME = "/{name}";
	public static final String CONFIG = "/config" + NAME;
	public static final String I18N = "/i18n" + NAME;
	public static final String UTILPAGE = "/utilpage";
	public static final String MENU = "/menu";
	public static final String BANNER = "/banner";
	public static final String ADD_IMAGE = "/add-image";
	public static final String ADD_FOLDER = "/add-folder";

	public static final String ADMIN = "/mnn";
	public static final String ADMIN_LOGIN = ADMIN + "/login";
	public static final String ADMIN_LOGOUT = ADMIN + "/logout";
	public static final String DASHBOARD = ADMIN + "/dashboard";
	public static final String ADMIN_CATEGORIES = ADMIN + "/categories";
	public static final String ADMIN_ARTICLES = ADMIN + "/articles";
	public static final String ADMIN_SETTING = ADMIN + SETTING;
	public static final String ADMIN_IMAGE_FOLDER = ADMIN + "/image-folder";
	public static final String ADMIN_ACCOUNT = ADMIN + ACCOUNT;
	public static final String ADMIN_ACCOUNTS = ADMIN + ACCOUNT + "s";
	public static final String ADMIN_COMMENTS = ADMIN + COMMENTS;

}