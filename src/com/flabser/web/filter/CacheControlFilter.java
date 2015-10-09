package com.flabser.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CacheControlFilter implements Filter {

	private static final long ONE_DAY_MS = 86400000L;
	private static final long ONE_YEAR_MS = ONE_DAY_MS * 365;

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestURI = httpRequest.getRequestURI();

		if (requestURI.contains("nocache")) {
			long now = System.currentTimeMillis();
			httpResponse.setDateHeader("Date", now);
			httpResponse.setDateHeader("Expires", now - ONE_DAY_MS);
			httpResponse.setHeader("Cache-control", "public, max-age=0, must-revalidate");

		} else if (cacheResources(requestURI)) {
			long now = System.currentTimeMillis();
			httpResponse.setDateHeader("Date", now);
			httpResponse.setDateHeader("Expires", now + ONE_YEAR_MS);

			if (httpResponse.containsHeader("Pragma")) {
				httpResponse.setHeader("Pragma", null);
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean cacheResources(String requestUri) {
		String[] pattern = new String[] { ".ico", ".jpg", ".jpeg", ".png", ".gif", ".js", ".css" };

		for (String p : pattern) {
			if (requestUri.endsWith(p)) {
				return true;
			}
		}

		if (requestUri.indexOf("/fonts/") != -1) {
			return true;
		}

		return false;
	}

	public void destroy() {
	}
}
