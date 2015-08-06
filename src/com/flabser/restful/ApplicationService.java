package com.flabser.restful;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.omg.CORBA.UserException;

import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.SessionPool;
import com.flabser.users.UserSession;

@Path("/session")
public class ApplicationService {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AuthUser getSession() {
		HttpSession jses = request.getSession(true);

		AuthUser user = new AuthUser();
		UserSession userSession = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
		if (userSession == null) {
			return user;
		}

		user.setLogin(userSession.currentUser.getLogin());
		return user;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AuthUser createSession(AuthUser authUser) throws ClassNotFoundException, InstantiationException, DatabasePoolException, UserException,
			IllegalAccessException, SQLException {

		return authUser;
	}

	@DELETE
	public void destroySession() {
		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
		if (userSession != null) {
			jses.removeAttribute(UserSession.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}
	}
}
