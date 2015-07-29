package com.flabser.filters;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
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
			if (http.getRequestURI().contains("session") || http.getRequestURI().contains("page") || http.getRequestURI().contains("Provider")
					|| http.getRequestURI().contains("js") || http.getRequestURI().contains("css")) {
				// try {
				// String v =
				// Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890");
				// Server.webServerInst.addApplication("CashTracker",
				// "/CashTracker/" + v, "CashTracker");
				// System.out.println("CashTracker/" + v);
				// } catch (LifecycleException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				chain.doFilter(request, resp);
			} else {

				/*
				 * ServletContext srcServletContext = http.getServletContext();
				 * ServletContext targetServletContext =
				 * srcServletContext.getContext("/Nubis");
				 * 
				 * System.out.println(srcServletContext.getContextPath() + " = "
				 * + targetServletContext.getAttribute("test"));
				 */

				String v = http.getRequestURI();
				String g = v.substring(1, v.length());
				String appType = g.substring(0, g.indexOf("/"));
				String appID = g.substring(appType.length() + 1, g.indexOf("/index.html"));

				HttpSession jses = http.getSession(false);
				if (jses != null) {
					UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
					if (us != null) {

						AppEnv env = Environment.getApplication(appType);
						// AppEnv env = (AppEnv)http.getServletContext();
						// context.getAttribute(AppEnv.APP_ATTR);
						if (env.appType.equals(AppEnv.ADMIN_APP_NAME)) {
							if (us.currentUser.isSupervisor()) {
								chain.doFilter(request, resp);
							} else {
								HttpServletResponse httpResponse = (HttpServletResponse) resp;
								httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								Server.logger.warningLogEntry("User is not Administrator");
							}
						} else {
							if (us.isBootstrapped(appID)) {
								Server.logger.warningLogEntry("session alive ...");
								chain.doFilter(request, resp);
							} else {
								HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.appType);
								if (hh != null) {
									Server.logger.warningLogEntry("database initializing ...");
									// us.init((String)
									// request.getAttribute("appid"));
									us.init(appID);
									try {
										Context context = Server.webServerInst.addApplication(appID, env.appType);
										context.getServletContext().setAttribute(AppEnv.APP_ATTR, env);
									} catch (LifecycleException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									System.out.println("request again " + v);
									((HttpServletResponse) resp).sendRedirect(v);
								} else {
									HttpServletResponse httpResponse = (HttpServletResponse) resp;
									httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
									Server.logger.warningLogEntry("access to application '" + env.appType + "' restricted");
								}
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
						} else {
							HttpServletResponse httpResponse = (HttpServletResponse) resp;
							httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							Server.logger.warningLogEntry("There is no user session ");
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
