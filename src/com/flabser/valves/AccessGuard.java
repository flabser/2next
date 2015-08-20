package com.flabser.valves;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
		String requestURI = http.getRequestURI();
		String params = http.getQueryString();

		if (params == null) {
			chain.doFilter(request, response);
		} else {
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
				if (us != null && us.currentUser.isSupervisor()) {
					chain.doFilter(request, response);
				} else {
					throw new AuthFailedException(AuthFailedExceptionType.ACCESS_DENIED, EnvConst.ADMIN_APP_NAME);

				}
			} else {
				throw new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, EnvConst.ADMIN_APP_NAME);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
