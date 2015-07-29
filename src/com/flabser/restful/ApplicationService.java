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
import com.flabser.solutions.DatabaseType;
import com.flabser.users.AuthFailedException;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;
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
import java.sql.SQLException;
import java.util.HashMap;

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
		UserSession userSession = null;
		HttpSession jses;
		String appID = authUser.getDefaultApp();
		AppEnv env = (AppEnv) context.getAttribute(AppEnv.APP_ATTR);
		ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
		User user = systemDatabase.checkUserHash(authUser.getLogin(), authUser.getPwd(), "");

		if (!user.isAuthorized) {
			throw new AuthFailedException(authUser);
		}

		String userID = user.getLogin();
		jses = request.getSession(true);

		Server.logger.normalLogEntry(userID + " has connected");
		IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
		ua.postLogin(ServletUtil.getClientIpAddr(request), user);
		if (user.getStatus() == UserStatusType.REGISTERED) {
			if (!env.appType.equalsIgnoreCase("administrator")) {
				HashMap<String, ApplicationProfile> apps = user.getApplicationProfiles(env.appType);
				// TODO Need to add redirect to choice certainly application if
				// apps size > 1

				if (apps == null) {
					ApplicationProfile ap = new ApplicationProfile();
					ap.appType = env.appType;
					ap.appID = Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890");
					ap.appName = env.appType + " of " + user.getLogin();
					ap.owner = user.getLogin();
					ap.dbLogin = (user.getLogin().replace("@", "_").replace(".", "_").replace("-", "_")).toLowerCase();
					ap.dbType = DatabaseType.POSTGRESQL;
					ap.dbName = ap.appType.toLowerCase() + "_" + ap.appID;
					ap.dbPwd = user.getDefaultDbPwd();
					ap.save();
					user.addApplication(ap);
					user.save();
					authUser.setRedirect("setup");
				} else {
					authUser.setApplications(apps);
					if (appID == null && apps.values().size() == 1) {
						appID = apps.values().iterator().next().appID;
						authUser.setDefaultApp(appID);
					}
				}
			}
		} else if (user.getStatus() == UserStatusType.WAITING_FOR_FIRST_ENTERING) {
			authUser.setRedirect("tochangepwd");
		} else if (user.getStatus() == UserStatusType.NOT_VERIFIED || user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
			throw new AuthFailedException(authUser);
		} else if (user.getStatus() == UserStatusType.DELETED) {
			throw new AuthFailedException(authUser);
		}

		userSession = new UserSession(user, appID, jses);
		SessionPool.put(userSession);
		jses.setAttribute(UserSession.SESSION_ATTR, userSession);

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
