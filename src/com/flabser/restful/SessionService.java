package com.flabser.restful;

import java.sql.SQLException;
import java.util.HashMap;

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

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.SessionPool;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.solutions.DatabaseType;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

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
		HttpSession jses = request.getSession(true);

		AuthUser user = new AuthUser();
		UserSession userSession = (UserSession) jses
				.getAttribute(UserSession.SESSION_ATTR);
		if (userSession == null) {
			return user;
		}

		user.setLogin(userSession.currentUser.getLogin());
		return user;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSession(AuthUser authUser)
			throws ClassNotFoundException, InstantiationException,
			DatabasePoolException, UserException, IllegalAccessException,
			SQLException {
		UserSession userSession = null;
		HttpSession jses;
		String appID = ""; // temporary variable

		AppEnv env = (AppEnv) context.getAttribute(AppEnv.APP_ATTR);
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		User user = new User();
		user = systemDatabase.checkUserHash(authUser.getLogin(),
				authUser.getPwd(), "", user);

		if (!user.isAuthorized) {
			throw new AuthFailedException(
					AuthFailedExceptionType.PASSWORD_INCORRECT,
					authUser.getLogin());
		}

		String userID = user.getLogin();
		jses = request.getSession(true);

		Server.logger.normalLogEntry(userID + " has connected");
		IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
		ua.postLogin(ServletUtil.getClientIpAddr(request), user);
		if (user.getStatus() == UserStatusType.REGISTERED) {
			if (!env.appType.equalsIgnoreCase("administrator")) {
				HashMap<String, ApplicationProfile> apps = user
						.getApplicationProfiles(env.appType);
				// TODO Need to add redirect to choice certainly application if
				// apps size > 1

				if (apps == null) {
					ApplicationProfile ap = new ApplicationProfile();
					ap.appType = env.appType;
					ap.appID = Util
							.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890");
					ap.appName = env.appType + " of " + user.getLogin();
					ap.owner = user.getLogin();
					ap.dbLogin = (user.getLogin().replace("@", "_")
							.replace(".", "_").replace("-", "_")).toLowerCase();
					ap.dbType = DatabaseType.POSTGRESQL;
					ap.dbName = ap.appType.toLowerCase() + "_" + ap.appID;
					ap.dbPwd = Util
							.generateRandomAsText("QWERTYUIOPASDFGHJKLMNBVCXZ1234567890");
					ap.save();
					user.addApplication(ap);
					user.save();
					authUser.setRedirect("setup");
				} else {
					authUser.setApplications(apps);
					appID = apps.values().iterator().next().appID;
				}
			}
		} else if (user.getStatus() == UserStatusType.WAITING_FOR_FIRST_ENTERING) {
			authUser.setRedirect("tochangepwd");
		} else if (user.getStatus() == UserStatusType.NOT_VERIFIED
				|| user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
			throw new AuthFailedException(AuthFailedExceptionType.NOT_VERIFED,
					authUser.getLogin());
		} else if (user.getStatus() == UserStatusType.DELETED) {
			throw new AuthFailedException(AuthFailedExceptionType.DELETED,
					authUser.getLogin());
		}

		userSession = new UserSession(user, appID, jses);
		SessionPool.put(userSession);
		jses.setAttribute(UserSession.SESSION_ATTR, userSession);

		return Response.ok(authUser)
				.cookie(new NewCookie("lang", userSession.getLang())).build();
	}

	@DELETE
	public void destroySession() {
		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses
				.getAttribute(UserSession.SESSION_ATTR);
		if (userSession != null) {
			jses.removeAttribute(UserSession.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}
	}
}
