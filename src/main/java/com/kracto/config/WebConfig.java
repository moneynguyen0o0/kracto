package com.kracto.config;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import com.kracto.dto.ViewCounter;
import com.kracto.web.constant.Keyword;
import com.kracto.web.constant.URL;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan("com.kracto.web.controller")
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tiles = new TilesConfigurer();
		tiles.setDefinitions("/WEB-INF/layouts/tiles.xml");
		tiles.setCheckRefresh(true);
		return tiles;
	}

	@Bean
	public ViewResolver viewResolver() {
		TilesViewResolver tilesViewResolver = new TilesViewResolver();
		tilesViewResolver.setOrder(0);
		return tilesViewResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasenames("classpath:i18n/message", "classpath:i18n/validation", "classpath:i18n/menu", "classpath:i18n/banner", "classpath:i18n/page");
		resource.setDefaultEncoding("UTF-8");
		return resource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(new Locale(Keyword.VI));
		resolver.setCookieName("language");
		resolver.setCookieMaxAge(3600);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean(name = "validator")
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}

	@Override
	public Validator getValidator() {
		return validator();
	}

	@Bean(name = "filterMultipartResolver")
	public MultipartResolver multipartResolver() throws IOException {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		// 5Mb file limit
		// multipartResolver.setMaxUploadSize(1024 * 1024 * 5);
		// After 1Mb start writing files to disk
		// multipartResolver.setMaxInMemorySize(1024 * 1024);
		multipartResolver.setDefaultEncoding("utf-8");
		return multipartResolver;
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// http://stackoverflow.com/questions/24537877/spring-serving-static-content-while-having-wildcard-controller-route
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registry.addResourceHandler(URL.IMAGES + "/**").addResourceLocations("/resources/images/");
		registry.addResourceHandler(URL.CSS + "/**").addResourceLocations("/resources/css/").setCachePeriod(604800);
		registry.addResourceHandler(URL.JS + "/**").addResourceLocations("/resources/js/").setCachePeriod(604800);
		registry.addResourceHandler(URL.FONTS + "/**").addResourceLocations("/resources/fonts/").setCachePeriod(604800);
		;
		registry.addResourceHandler(URL.CKEDITOR + "/**").addResourceLocations("/resources/ckeditor/");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldType(Date.class, new DateFormatter("dd/MM/yyyy"));
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public ViewCounter visitor() {
		return new ViewCounter();
	}

}