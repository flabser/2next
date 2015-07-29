package com.flabser.restful;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import org.omg.CORBA.UserException;

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
import java.sql.SQLException;
import java.util.HashMap;

@Path("/session")
public class SessionService {

	private static final String AUTH_COOKIE_NAME = "2nses";
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
	public Response createSession(AuthUser authUser) throws ClassNotFoundException, InstantiationException, DatabasePoolException, UserException,
			IllegalAccessException, SQLException {
		UserSession userSession = null;
		HttpSession jses;
		String appID = authUser.getDefaultApp();
		context.getAttribute(AppEnv.APP_ATTR);
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
		if (user.getStatus() == UserStatusType.REGISTERED) {
			HashMap<String, ApplicationProfile> apps = user.getApplicationProfiles();
			authUser.setApplications(apps);
			authUser.setDefaultApp(appID);
			authUser.setStatus(user.getStatus());

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

		userSession = new UserSession(user, jses);
		String token = SessionPool.put(userSession);
		jses.setAttribute(UserSession.SESSION_ATTR, userSession);
		// context.setAttribute("test", "zzzz");
		int maxAge = -1;

		NewCookie cookie = new NewCookie(AUTH_COOKIE_NAME, token, "/", null, null, maxAge, false);

		return Response.status(HttpServletResponse.SC_OK).entity(authUser).cookie(cookie).build();
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
