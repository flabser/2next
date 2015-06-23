package com.flabser.restful;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.flabser.appenv.AppEnv;
import com.flabser.script._Session;
import com.flabser.users.UserSession;

@Path("/")
public class RestProvider {
	@Context
	private ServletContext context;
	@Context
	public HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	public AppEnv getAppEnv() {
		return (AppEnv) context.getAttribute("portalenv");

	}

	public _Session getSession() {
		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");
		if (userSession == null) {			
			return null;
		} else {
			return new _Session(getAppEnv(), userSession);
		}
	}

}
