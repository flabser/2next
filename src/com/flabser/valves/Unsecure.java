package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.exception.ApplicationException;
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
		if (!ru.getUrl().equals("/")) {
			Site site = Environment.availableTemplates.get(ru.getAppType());
			if (site == null) {
				String rh = http.getServerName();
				site = Environment.availableTemplates.get(rh);
			}

			if (site != null) {
				ru.setAppType(site.getAppBase());
				if (ru.isAuthRequest()) {
					if (http.getMethod().equalsIgnoreCase("POST")) {
						HttpSession jses = http.getSession(true);
						jses.setAttribute(EnvConst.SESSION_ATTR, new UserSession(new User()));
						getNext().getNext().invoke(request, response);
					} else {
						((Secure) getNext()).invoke(request, response, ru);
					}
				} else {
					if (ru.isProtected()) {
						if (ru.isPage()) {
							try {
								if (site.getAppTemlate().ruleProvider.getRule(ru.getPageID()).isAnonymousAllowed()) {
									gettingSession(request, response);
									getNext().getNext().invoke(request, response);
								} else {
									((Secure) getNext()).invoke(request, response, ru);
								}

							} catch (RuleException e) {
								Server.logger.errorLogEntry(e.getMessage());
								ApplicationException ae = new ApplicationException(ru.getAppType(), e.getMessage());
								response.setStatus(ae.getCode());
								response.getWriter().println(ae.getHTMLMessage());
							}
						} else {
							((Secure) getNext()).invoke(request, response, ru);
						}
					} else {
						gettingSession(request, response);
						getNext().getNext().invoke(request, response);
					}
				}
			} else if (ru.getAppType().equals(EnvConst.SHARED_RESOURCES_NAME)
					|| ru.getAppType().equals(EnvConst.ADMIN_APP_NAME)) {
				getNext().getNext().invoke(request, response);
			} else {
				String msg = "Unknown application type \"" + ru.getAppType() + "\"";
				Server.logger.warningLogEntry(msg);
				ApplicationException ae = new ApplicationException(ru.getAppType(), msg);
				response.setStatus(ae.getCode());
				response.getWriter().println(ae.getHTMLMessage());
			}
		} else  {
			getNext().getNext().invoke(request, response);
		}
	}

	private void gettingSession(Request request, Response response) {
		HttpSession jses = request.getSession(false);
		if (jses == null) {
			jses = http.getSession(true);
			jses.setAttribute(EnvConst.SESSION_ATTR, new UserSession(new User()));
		} else {
			UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
			if (us == null) {
				jses.setAttribute(EnvConst.SESSION_ATTR, new UserSession(new User()));
			}
		}

	}
}
