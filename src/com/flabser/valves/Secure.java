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
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.server.Server;
import com.flabser.servlets.SessionCooksValues;
import com.flabser.users.User;
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

		if (!appType.equalsIgnoreCase("") && !appType.equalsIgnoreCase(EnvConst.ADMIN_APP_NAME)) {
			HttpSession jses = http.getSession(false);
			if (jses != null) {
				UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
				if (us != null && !us.currentUser.getLogin().equals(User.ANONYMOUS_USER)) {
					if (!us.isBootstrapped(appID) && !appType.equalsIgnoreCase(EnvConst.WORKSPACE_APP_NAME)) {
						AppTemplate env = Environment.getAppTemplate(appType);
						HashMap<String, ApplicationProfile> hh = us.currentUser.getApplicationProfiles(env.templateType);
						if (hh != null) {
							Server.logger.verboseLogEntry("start application initializing ...");
							try {
								Server.webServerInst.addApplication(appID, env);
								us.init(appID);
								Server.logger.verboseLogEntry("application ready on: " + ru.getUrl());
								((HttpServletResponse) response).sendRedirect(ru.getUrl());
							} catch (ApplicationException e) {
								Server.logger.errorLogEntry(e.getMessage());
								response.setStatus(e.getCode());
								response.getWriter().println(e.getHTMLMessage());
							} catch (Exception e) {
								Server.logger.errorLogEntry(e.getMessage());
								ApplicationException ae = new ApplicationException(ru.getAppType(), e.getMessage());
								response.setStatus(ae.getCode());
								response.getWriter().println(ae.getHTMLMessage());
							}
						} else {
							String msg = "\"" + env.templateType + "\" has not set for \"" + us.currentUser.getLogin() + "\" (" + ru + ")";
							Server.logger.warningLogEntry(msg);
							ApplicationException e = new ApplicationException(ru.getAppType(), msg);
							response.setStatus(e.getCode());
							response.getWriter().println(e.getHTMLMessage());
						}
					} else {
						getNext().invoke(request, response);
					}
				} else {
					gettingSession(request, response);
				}
			} else {
				gettingSession(request, response);
			}
		} else {
			getNext().invoke(request, response);
		}

	}

	private void gettingSession(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		SessionCooksValues appCookies = new SessionCooksValues(http);
		String token = appCookies.auth;
		if (token != null) {
			UserSession userSession = SessionPool.getLoggeedUser(token);
			if (userSession != null) {
				HttpSession jses = http.getSession(true);
				jses.setAttribute(EnvConst.SESSION_ATTR, userSession);
				Server.logger.verboseLogEntry(userSession.toString() + "\" got from session pool "
						+ jses.getServletContext().getContextPath());
				invoke(request, response);
			} else {
				Server.logger.warningLogEntry("there is no associated user session for the token");
				AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.NO_ASSOCIATED_SESSION_FOR_THE_TOKEN,
						ru.getAppType());
				response.setStatus(e.getCode());
				response.getWriter().println(e.getHTMLMessage());
			}
		} else {
			Server.logger.warningLogEntry("user session was expired");
			AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, ru.getAppType());
			response.setStatus(e.getCode());
			response.getWriter().println(e.getHTMLMessage());
		}
	}
}
