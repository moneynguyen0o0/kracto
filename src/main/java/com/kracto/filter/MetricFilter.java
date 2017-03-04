package com.kracto.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kracto.service.MetricService;
import com.kracto.web.constant.URL;

@Component
public class MetricFilter implements Filter {

	@Autowired
	private MetricService metricService;

	@Override
	public void init(FilterConfig config) throws ServletException {
		if (metricService == null)
			metricService = (MetricService) WebApplicationContextUtils
					.getRequiredWebApplicationContext(config.getServletContext()).getBean("metricService");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws java.io.IOException, ServletException {
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String req = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
		chain.doFilter(request, response);
		if (StringUtils.indexOfAny(req,
				new String[] { URL.ADMIN, URL.IMAGES, URL.CSS, URL.JS, URL.FONTS, URL.CKEDITOR }) == -1) {
			int status = ((HttpServletResponse) response).getStatus();
			metricService.increaseCount(req, status);
		}
	}

	@Override
	public void destroy() {
	}

}
