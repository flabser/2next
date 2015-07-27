package com.flabser.filters;

import java.io.IOException;
import java.util.HashMap;

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
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.UserSession;

public class AccessGuard implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) {
		try {
			HttpServletRequest http = (HttpServletRequest) request;
			Server.logger.normalLogEntry(" filter " + http.getMethod() + " " + http.getRequestURI());
			if (http.getRequestURI().contains("session") || http.getRequestURI().contains("page") || http.getRequestURI().contains("Provider")) {
				chain.doFilter(request, resp);
			} else {

				ServletContext srcServletContext = http.getServletContext();
				ServletContext targetServletContext = srcServletContext.getContext("/Nubis");

				System.out.println(srcServletContext.getContextPath() + " = " + targetServletContext.getAttribute("test"));

				HttpSession jses = http.getSession(false);
				if (jses != null) {
					UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
					if (us != null) {
						ServletContext context = http.getServletContext();

						AppEnv env = (AppEnv) context.getAttribute(AppEnv.APP_ATTR);
						if (us.isAppAllowed(env.appType)) {
							Server.logger.warningLogEntry("session alive ...");
							chain.doFilter(request, resp);
						} else {
							HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.appType);
							if (hh.size() > 0) {
								Server.logger.warningLogEntry("database initializing ...");
								us.init((String) request.getAttribute("appid"));
								chain.doFilter(request, resp);
							} else {
								HttpServletResponse httpResponse = (HttpServletResponse) resp;
								httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								Server.logger.warningLogEntry("access to application '" + env.appType + "' restricted");
							}
						}
					} else {
						HttpServletResponse httpResponse = (HttpServletResponse) resp;
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger.warningLogEntry("user session was expired");
					}

				} else {
					Cookies appCookies = new Cookies(http);
					String token = appCookies.auth;
					if (token.length() > 0) {
						UserSession userSession = SessionPool.getLoggeedUser(token);
						if (userSession != null) {
							jses = http.getSession(true);
							jses.setAttribute(UserSession.SESSION_ATTR, userSession);
							Server.logger.verboseLogEntry("user session \"" + userSession.toString() + "\" got from session pool");
							doFilter(request, resp, chain);
						}
					} else {
						HttpServletResponse httpResponse = (HttpServletResponse) resp;
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						Server.logger.warningLogEntry("User session was expired");
					}
				}

			}
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabasePoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}
}
