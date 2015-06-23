package com.flabser.restful;

import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.omg.CORBA.UserException;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.servlets.Cookies;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

@Path("/")
public class Login {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;	

	@POST
	@Path("/signins")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public com.flabser.restful.SignIn producePost(com.flabser.restful.SignIn signUser) {
		UserSession userSession = null;
		try {
			System.out.println(request.getRequestedSessionId() + "  " + signUser.getClass().getName());
			HttpSession jses;

			AppEnv env = (AppEnv) context.getAttribute("portalenv");
			ISystemDatabase systemDatabase = DatabaseFactory.getSysDatabase();
			User user = new User();
			Cookies appCookies = new Cookies(request);
			user = systemDatabase.checkUserHash(signUser.getLogin(), signUser.getPwd(), appCookies.authHash, user);

			if (!user.isAuthorized)
				throw new AuthFailedException(AuthFailedExceptionType.PASSWORD_INCORRECT, signUser.getLogin());

			String userID = user.getLogin();
			jses = request.getSession(true);

			AppEnv.logger.normalLogEntry(userID + " has connected");
			IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
			ua.postLogin(ServletUtil.getClientIpAddr(request), user);
			if (user.getStatus() == UserStatusType.REGISTERED) {
				ApplicationProfile app = user.enabledApps.get(env.appType);
				if (app == null) {
					ApplicationProfile ap = new ApplicationProfile();
					ap.appName = env.appType;
					ap.owner = user.getLogin();
					ap.dbLogin = (user.getLogin().replace("@", "_").replace(".", "_").replace("-", "_")).toLowerCase();
					ap.dbName = ap.appName.toLowerCase() + "_" + ap.dbLogin;
					ap.dbPwd = Util.generateRandomAsText("QWERTYUIOPASDFGHJKLMNBVCXZ1234567890");
					ap.save();
					user.addApplication(ap);
					user.save();
				}
			} else if (user.getStatus() == UserStatusType.NOT_VERIFIED
					|| user.getStatus() == UserStatusType.WAITING_FOR_VERIFYCODE) {
				throw new AuthFailedException(AuthFailedExceptionType.NOT_VERIFED, signUser.getLogin());
			} else if (user.getStatus() == UserStatusType.DELETED) {
				throw new AuthFailedException(AuthFailedExceptionType.DELETED, signUser.getLogin());
			}

            userSession = new UserSession(user, env.globalSetting.implementation, env.appType);
			jses.setAttribute("usersession", userSession);

		} catch (AuthFailedException ae) {
			try {
				signUser.setStatus(ae.type);
				signUser.setError(ae.getMessage());
			} catch (IllegalStateException ise) {
				// new PortalException(ise, env, response, PublishAsType.HTML);
			} catch (Exception e) {
				// new PortalException(e, response,
				// ProviderExceptionType.INTERNAL,
				// PublishAsType.HTML);
			}	

		} catch (ClassNotFoundException e) {		
			e.printStackTrace();
		} catch (InstantiationException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (UserException e) {			
			e.printStackTrace();
		} catch (DatabasePoolException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}

		return signUser;

	}
}
