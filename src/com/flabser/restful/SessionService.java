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
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.omg.CORBA.UserException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.env.SessionPool;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;

@Path("/session")
public class SessionService {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AuthUser getSession() {
		HttpSession jses = request.getSession(false);
		UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		if (userSession == null) {
			return new AuthUser();
		}
		return userSession.getUserPOJO();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSession(AuthUser authUser) throws ClassNotFoundException, InstantiationException, DatabasePoolException,
			UserException, IllegalAccessException, SQLException {
		UserSession userSession = null;
		HttpSession jses;
		String appID = authUser.getDefaultApp();
		context.getAttribute(EnvConst.TEMPLATE_ATTR);
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		String login = authUser.getLogin();
		Server.logger.normalLogEntry(login + " is attempting to signin");
		User user = systemDatabase.checkUserHash(login, authUser.getPwd(), null);
		authUser.setPwd(null);
		if (!user.isAuthorized) {
			Server.logger.warningLogEntry("signin of " + login + " was failed");
			authUser.setError(AuthFailedExceptionType.PASSWORD_OR_LOGIN_INCORRECT);
			throw new AuthFailedException(authUser);
		}

		String userID = user.getLogin();
		jses = request.getSession(true);

		Server.logger.normalLogEntry(userID + " has connected");
		IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
		ua.postLogin(ServletUtil.getClientIpAddr(request), user);
		userSession = new UserSession(user);
		if (user.getStatus() == UserStatusType.REGISTERED) {
			authUser = userSession.getUserPOJO();
			authUser.setDefaultApp(appID);
		} else if (user.getStatus() == UserStatusType.WAITING_FOR_FIRST_ENTERING) {
			authUser.setRedirect("tochangepwd");
		} else if (user.getStatus() == UserStatusType.NOT_VERIFIED) {
			authUser.setError(AuthFailedExceptionType.INCOMPLETE_REGISTRATION);
			throw new AuthFailedException(authUser);
		} else if (user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
			authUser.setError(AuthFailedExceptionType.INCOMPLETE_REGISTRATION);
			throw new AuthFailedException(authUser);
		} else if (user.getStatus() == UserStatusType.DELETED) {
			authUser.setError(AuthFailedExceptionType.NOT_FOUND);
			throw new AuthFailedException(authUser);
		}

		String token = SessionPool.put(userSession);
		jses.setAttribute(EnvConst.SESSION_ATTR, userSession);
		int maxAge = -1;

		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, token, "/", null, null, maxAge, false);

		return Response.status(HttpServletResponse.SC_OK).entity(authUser).cookie(cookie).build();
	}

	@DELETE
	public Response destroySession() {
		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		if (userSession != null) {
			jses.removeAttribute(EnvConst.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}
		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, "", "/", null, null, 0, false);
		return Response.status(HttpServletResponse.SC_OK).cookie(cookie).build();

	}
}
