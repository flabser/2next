package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.server.Server;

public class Unsecure extends ValveBase {
	RequestURL ru;

	public void invoke(Request request, Response response, RequestURL ru) throws IOException, ServletException {
		this.ru = ru;
		invoke(request, response);
	}

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		String requestURI = http.getRequestURI();
		String params = http.getQueryString();
		if (params != null) {
			requestURI = requestURI + "?" + http.getQueryString();
		}
		RequestURL ru = new RequestURL(requestURI);

		if ((!ru.isProtected()) || ru.isAuthRequest()) {
			getNext().getNext().invoke(request, response);
		} else {
			if (ru.isPage()) {
				AppTemplate aTemplate = Environment.getApplication(ru.getAppType());
				try {
					if (aTemplate.ruleProvider.getRule(ru.getPageID()).isAnonymousAllowed()) {
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
}
