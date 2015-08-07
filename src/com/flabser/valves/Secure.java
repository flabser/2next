package com.flabser.valves;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.UserSession;

public class Secure extends ValveBase {
	RequestURL ru;

	public void invoke(Request request, Response response, RequestURL ru) throws IOException, ServletException {
		this.ru = ru;
		invoke(request, response);
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		String appType = ru.getAppType();
		String appID = ru.getAppID();

		if (!appType.equalsIgnoreCase("") && !appType.equalsIgnoreCase(AppTemplate.ADMIN_APP_NAME)) {
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
				if (us != null) {
					if (!us.isBootstrapped(appID)) {
						AppTemplate env = Environment.getApplication(appType);
						HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.appType);
						if (hh != null) {
							Server.logger.verboseLogEntry("start application initializing ...");
							try {
								us.init(appID);
								Server.webServerInst.addApplication(appID, env);
								Server.logger.verboseLogEntry("application ready on: " + ru.getUrl());
								((HttpServletResponse) response).sendRedirect(ru.getUrl());
							} catch (Exception e) {
								Server.logger.errorLogEntry(e.getMessage());
								response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								response.getWriter().println(e.getMessage());
							}
						} else {
							String msg = "\"" + env.appType + "\" has not set for " + us.currentUser.getLogin();
							response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
							Server.logger.warningLogEntry(msg);
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().println(msg);
						}
					} else {
						getNext().invoke(request, response);
					}
				} else {
					restoreSession(request, response);
				}
			} else {
				restoreSession(request, response);
			}
		} else {
			getNext().invoke(request, response);
		}

	}

	private void restoreSession(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		Cookies appCookies = new Cookies(http);
		String token = appCookies.auth;
		if (token != null) {
			UserSession userSession = SessionPool.getLoggeedUser(token);
			if (userSession != null) {
				HttpSession jses = http.getSession(true);
				jses.setAttribute(UserSession.SESSION_ATTR, userSession);
				Server.logger.verboseLogEntry(userSession.toString() + "\" got from session pool " + jses.getServletContext().getContextPath());
				invoke(request, response);
			} else {
				Server.logger.warningLogEntry("there is no associated user session for the token");
				new AuthFailedException(AuthFailedExceptionType.NO_ASSOCIATED_SESSION_FOR_THE_TOKEN, response, ru.getAppType());
			}
		} else {
			Server.logger.warningLogEntry("user session was expired");
			new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, response, ru.getAppType());
		}
	}
}
