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
import javax.servlet.http.HttpSession;

import com.flabser.env.EnvConst;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.script._Session;
import com.flabser.server.Server;

public class AccessGuard implements Filter {
	HttpServletRequest http;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		http = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String requestURI = http.getRequestURI();

		// System.out.println(requestURI);
		// TODO it is need a regex
		if (requestURI.contains("/rest/")) {
			if (requestURI.equals("/rest/session") && http.getMethod().equalsIgnoreCase("POST")) {
				chain.doFilter(request, resp);
			} else {
				HttpSession jses = http.getSession(false);
				if (jses != null) {
					_Session us = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);
					if (us != null && us.getCurrentUser().isSupervisor()) {
						chain.doFilter(request, resp);
					} else {
						Server.logger.warningLogEntry("it was denied access");
						AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.ACCESS_DENIED,
								EnvConst.ADMIN_APP_NAME);
						resp.setStatus(e.getCode());
						resp.getWriter().println(e.getHTMLMessage());
					}
				} else {
					Server.logger.warningLogEntry("it was denied access");
					AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.ACCESS_DENIED,
							EnvConst.ADMIN_APP_NAME);
					resp.setStatus(e.getCode());
					resp.getWriter().println(e.getHTMLMessage());
				}
			}
		} else {
			chain.doFilter(request, resp);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}

}
