package com.flabser.valves;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import com.flabser.server.Server;

public class Unsecure extends ValveBase {

	@Override
	public void invoke(Request request, Response response) throws IOException, ServletException {
		HttpServletRequest http = request;
		String requestURI = http.getRequestURI();
		RequestURL ru = new RequestURL(requestURI);

		Server.logger.normalLogEntry(" valve " + http.getMethod() + " " + http.getRequestURI());
		if (!ru.isWebResource() && !ru.isAuth() && !ru.isTemplate()) {
			((Secure) getNext()).invoke(request, response, ru);
		} else {
			getNext().getNext().invoke(request, response);
		}
	}

}
