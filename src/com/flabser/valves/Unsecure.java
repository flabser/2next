package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

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
			// Server.logger.verboseLogEntry("free area");
			getNext().getNext().invoke(request, response);
		} else {
			if (ru.isPage()) {
				// Server.logger.verboseLogEntry("is Page");
				getNext().getNext().invoke(request, response);
			} else {
				// Server.logger.normalLogEntry(http.getMethod() + " " +
				// requestURI);
				// Server.logger.verboseLogEntry("not anonymous area");
				((Secure) getNext()).invoke(request, response, ru);
			}
		}
	}
}
