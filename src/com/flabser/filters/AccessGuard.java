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
import com.flabser.server.Server;
import com.flabser.users.UserSession;

public class AccessGuard implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse resp,
			FilterChain chain) {
		try {
			HttpServletRequest http = (HttpServletRequest) request;
			Server.logger.normalLogEntry(" Filter " + http.getMethod() + " "
					+ http.getRequestURI());
			if (http.getRequestURI().contains("session")
					|| http.getRequestURI().contains("page")
					|| http.getRequestURI().contains("Provider")) {
				chain.doFilter(request, resp);
			} else {
				HttpSession jses = http.getSession(false);
				if (jses != null) {
					UserSession us = (UserSession) jses
							.getAttribute(UserSession.SESSION_ATTR);
					if (us != null) {
						ServletContext context = http.getServletContext();
						AppEnv env = (AppEnv) context
								.getAttribute(AppEnv.APP_ATTR);
						if (us.isAppAllowed(env.appType)) {
							Server.logger.warningLogEntry("Session alive ...");
							chain.doFilter(request, resp);
						} else {
							HttpServletResponse httpResponse = (HttpServletResponse) resp;
							httpResponse
									.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							Server.logger
									.warningLogEntry("Access to application '"
											+ env.appType + "' restricted");
						}
					} else {
						HttpServletResponse httpResponse = (HttpServletResponse) resp;
						httpResponse
								.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger
								.warningLogEntry("User session was expired");
					}
				} else {
					HttpServletResponse httpResponse = (HttpServletResponse) resp;
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					Server.logger.warningLogEntry("User session was expired");
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
