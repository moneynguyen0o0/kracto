package com.kracto.config.core;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.kracto.config.CachingConfig;
import com.kracto.config.JdbcConfig;
import com.kracto.config.MailConfig;
import com.kracto.config.RootConfig;
import com.kracto.config.SocialConfig;
import com.kracto.config.WebConfig;
import com.kracto.filter.MetricFilter;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class, JdbcConfig.class, MailConfig.class, SocialConfig.class,
				CachingConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new MetricFilter() };
	}

}
