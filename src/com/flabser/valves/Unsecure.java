package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserSession;

public class Unsecure extends ValveBase {
	private RequestURL ru;
	private HttpServletRequest http;

	public void invoke(Request request, Response response, RequestURL ru) throws IOException, ServletException {
		this.ru = ru;
		invoke(request, response);
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		http = request;

		if ((!ru.isProtected()) || ru.isAuthRequest()) {
			gettingSession(request, response);
			getNext().getNext().invoke(request, response);
		} else {
			if (ru.isPage()) {
				AppTemplate aTemplate = Environment.getApplication(ru.getAppType());
				try {
					if (aTemplate.ruleProvider.getRule(ru.getPageID()).isAnonymousAllowed()) {
						gettingSession(request, response);
						getNext().getNext().invoke(request, response);
					} else {
						((Secure) getNext()).invoke(request, response, ru);
					}

				} catch (RuleException e) {
					Server.logger.errorLogEntry(e.getMessage());
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().println(e.getMessage());
				}

			} else {
				((Secure) getNext()).invoke(request, response, ru);
			}
		}
	}

	private void gettingSession(Request request, Response response) {
		HttpSession jses = request.getSession(false);
		if (jses == null) {
			jses = http.getSession(true);
			jses.setAttribute(UserSession.SESSION_ATTR, new UserSession(new User()));
		}
	}
}
