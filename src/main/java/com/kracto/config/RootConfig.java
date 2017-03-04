package com.kracto.config;

import java.util.regex.Pattern;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.context.annotation.ComponentScan.Filter;
import com.kracto.config.RootConfig.WebPackage;

@Configuration
@ComponentScan(basePackages = { "com.kracto" }, excludeFilters = {
		@Filter(type = FilterType.CUSTOM, value = WebPackage.class) })
@PropertySource({ "classpath:application.properties" })
public class RootConfig {
	public static class WebPackage extends RegexPatternTypeFilter {
		public WebPackage() {
			super(Pattern.compile("com\\.kracto\\.web"));
		}
	}
}