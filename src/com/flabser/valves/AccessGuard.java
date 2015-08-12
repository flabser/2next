package com.flabser.valves;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.env.EnvConst;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.users.UserSession;

public class AccessGuard implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest http = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestURI = http.getRequestURI();
		String params = http.getQueryString();

		if (params != null) {
			requestURI = requestURI + "?" + http.getQueryString();
		}

		// TODO need to improve RequestURL for the Admin
		RequestURL ru = new RequestURL(requestURI);
		if (ru.isDefault() || (!ru.isProtected())) {
			chain.doFilter(request, response);
		} else {
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
				if (us != null && us.currentUser.isSupervisor()) {
					chain.doFilter(request, response);
				} else {
					AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.ACCESS_DENIED, ru.getAppType());
					httpResponse.setStatus(e.getCode());
					httpResponse.getWriter().println(e.getHTMLMessage());
				}
			} else {
				AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, ru.getAppType());
				httpResponse.setStatus(e.getCode());
				httpResponse.getWriter().println(e.getHTMLMessage());
			}
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
