package com.flabser.valves;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.server.Server;

public class Logging extends ValveBase {

	public Logging() {
		super();
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

		Enumeration<String> headerNames = http.getHeaderNames();

		if (headerNames.hasMoreElements()) {  
			String header = headerNames.nextElement();
		//	Object v[] = new Object[] { header, http.getHeader(header) };
		//	Server.logger.normalLogEntry(v[0] + "=" + v[1]);
		}
		Server.logger.normalLogEntry(ru.getUrl());
		((Unsecure) getNext()).invoke(request, response, ru);
		return;
	}

}
