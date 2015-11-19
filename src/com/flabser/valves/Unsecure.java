package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.localization.LanguageType;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.servlets.SessionCooks;
import com.flabser.users.User;

public class Unsecure extends ValveBase {
	private RequestURL ru;

	public void invoke(Request request, Response response, RequestURL ru) throws IOException, ServletException {
		this.ru = ru;
		invoke(request, response);
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		request.setAttribute("appid", ru.getAppID());

		Site site = Environment.availableTemplates.get(ru.getAppType());
		if (site == null) {
			String rh = request.getServerName();
			site = Environment.availableTemplates.get(rh);
		}

		if (site != null) {
			ru.setAppType(site.getAppBase());

			if (ru.isAuthRequest()) {
				if (request.getMethod().equalsIgnoreCase("POST")) {
					HttpSession jses = request.getSession(true);
					jses.setAttribute(EnvConst.SESSION_ATTR,
							new _Session(site.getAppTemlate(), ru.getAppID(), new User()));
					getNext().getNext().invoke(request, response);
				} else {
					((Secure) getNext()).invoke(request, response, ru, site);
				}
			} else {
				if (ru.isPage()) {
					try {
						if (site.getAppTemlate().ruleProvider.getRule(ru.getPageID()).isAnonymousAllowed()) {
							gettingSession(request, response, site.getAppTemlate(), ru.getAppID());
							getNext().getNext().invoke(request, response);
						} else {
							((Secure) getNext()).invoke(request, response, ru, site);
						}

					} catch (RuleException e) {
						Server.logger.errorLogEntry(e.getMessage());
						ApplicationException ae = new ApplicationException(ru.getAppType(), e.getMessage());
						response.setStatus(ae.getCode());
						response.getWriter().println(ae.getHTMLMessage());
					}
				} else if (ru.isProtected()) {
					((Secure) getNext()).invoke(request, response, ru, site);
				} else {
					gettingSession(request, response, site.getAppTemlate(), ru.getAppID());
					getNext().getNext().invoke(request, response);
				}
			}

		} else if (ru.getAppType().equals(EnvConst.SHARED_RESOURCES_NAME)) {
			getNext().getNext().invoke(request, response);
		} else {
			String msg = "unknown application type \"" + ru.getAppType() + "\"";
			Server.logger.warningLogEntry(msg);
			ApplicationException ae = new ApplicationException(ru.getAppType(), msg);
			response.setStatus(ae.getCode());
			response.getWriter().println(ae.getHTMLMessage());
		}

	}

	private void gettingSession(Request request, Response response, AppTemplate env, String appID) {
		HttpSession jses = request.getSession(false);
		if (jses == null) {
			jses = request.getSession(true);
			jses.setAttribute(EnvConst.SESSION_ATTR, getAnonymousSes(request, response, jses, env, appID));
		} else {
			_Session us = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);
			if (us == null) {
				jses.setAttribute(EnvConst.SESSION_ATTR, getAnonymousSes(request, response, jses, env, appID));
			}
		}

	}

	private _Session getAnonymousSes(Request request, Response response, HttpSession jses, AppTemplate env,
			String appID) {
		SessionCooks cooks = new SessionCooks(request, response);
		_Session ses = new _Session(env, appID, new User());
		ses.setLang(LanguageType.valueOf(cooks.getCurrentLang()));
		return ses;
	}

}
