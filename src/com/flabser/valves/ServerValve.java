package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.env.EnvConst;
import com.flabser.env.SessionPool;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.servlets.SessionCooksValues;

public abstract class ServerValve extends ValveBase {
	RequestURL ru;

	protected void gettingSession(Request request, Response response, boolean allowAnonymous)
			throws IOException, ServletException {
		HttpServletRequest http = request;
		SessionCooksValues appCookies = new SessionCooksValues(http);
		String token = appCookies.auth;
		if (token != null) {
			_Session userSession = SessionPool.getLoggeedUser(token);
			if (userSession != null) {
				HttpSession jses = http.getSession(true);
				jses.setAttribute(EnvConst.SESSION_ATTR, userSession);
				Server.logger.debugLogEntry(userSession.toString() + "\" got from session pool "
						+ jses.getServletContext().getContextPath());
				invoke(request, response);
			} else {
				if (allowAnonymous) {
					// getAnnonymousSession(request, response);
				} else {
					Server.logger.warningLogEntry("there is no associated user session for the token");
					AuthFailedException e = new AuthFailedException(
							AuthFailedExceptionType.NO_ASSOCIATED_SESSION_FOR_THE_TOKEN, ru.getAppType());
					response.setStatus(e.getCode());
					response.getWriter().println(e.getHTMLMessage());
				}
			}
		} else {
			if (allowAnonymous) {
				// getAnnonymousSession(request, response);
			} else {
				Server.logger.warningLogEntry("user session was expired");
				AuthFailedException e = new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION,
						ru.getAppType());
				response.setStatus(e.getCode());
				response.getWriter().println(e.getHTMLMessage());
			}
		}
	}

}
