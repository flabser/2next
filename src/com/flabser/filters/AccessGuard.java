package com.flabser.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.appenv.AppEnv;
import com.flabser.users.UserSession;


public class AccessGuard implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) {
		try {
			HttpServletRequest http = (HttpServletRequest) request;
			AppEnv.logger.errorLogEntry(" Filter method=" + http.getMethod() + " " + http.getRequestURI());
			if (http.getRequestURI().contains("session") || http.getRequestURI().contains("page")
					|| http.getRequestURI().contains("Provider")) {
				chain.doFilter(request, resp);
			} else {
				HttpSession jses = http.getSession(true);
				UserSession us = (UserSession) jses.getAttribute("usersession");
				if (us != null) {
					chain.doFilter(request, resp);
				} else {
					HttpServletResponse httpResponse = (HttpServletResponse) resp;
					ServletContext context = http.getServletContext();
					AppEnv env = (AppEnv) context.getAttribute("portalenv");
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					AppEnv.logger.errorLogEntry(" Application '" + env.appType + "' access denied");
				}

			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}
}
