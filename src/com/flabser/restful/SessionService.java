package com.flabser.restful;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import nubis.page.app.ResetPasswordEMail;
import nubis.page.app.VerifyEMail;

import org.apache.commons.io.FileUtils;
import org.omg.CORBA.UserException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.exception.WebFormValueException;
import com.flabser.restful.pojo.AppUser;
import com.flabser.restful.pojo.Outcome;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.script._Helper;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

@Path("/session")
public class SessionService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentSession() {
		HttpSession jses = request.getSession(false);
		UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		AppUser au = null;
		if (userSession == null) {
			au = new AppUser();
		} else {
			au = userSession.getUserPOJO();
		}
		return Response.status(HttpServletResponse.SC_OK).entity(au).build();

	}

	@GET
	@Path("/avatar")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile() {
		File file = null;
		String fn = null;

		HttpSession jses = request.getSession(false);
		UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		User user = userSession.currentUser;
		if (user.getLogin().equals(User.ANONYMOUS_USER)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		} else {
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			fn = userTmpDir.getAbsolutePath() + File.separator + user.getAvatar().getRealFileName();
			File fileToWriteTo = new File(fn);
			byte[] fileAsByteArray = DatabaseFactory.getSysDatabase().getUserAvatarStream(user.id);
			if (fileAsByteArray != null) {
				try {
					FileUtils.writeByteArrayToFile(fileToWriteTo, fileAsByteArray);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}
				file = new File(fn);
			}
		}

		if (file != null && file.exists()) {
			TempFileCleaner.addFileToDelete(fn);
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
		} else {
			return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
		}
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSession(AppUser appUser) throws WebFormValueException {
		HttpSession jses = request.getSession(false);
		UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		User user = userSession.currentUser;
		if (user.getLogin().equals(User.ANONYMOUS_USER)) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		} else {
			user.refresh(appUser);
			if (user.save()) {
				return Response.status(HttpServletResponse.SC_OK).entity(appUser).build();
			} else {
				return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
			}
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSession(AppUser authUser) throws ClassNotFoundException, InstantiationException,
	DatabasePoolException, UserException, IllegalAccessException, SQLException {
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
		} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_INVITATION ||
				user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD) {
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
		} else {
			authUser.setError(AuthFailedExceptionType.UNKNOWN_STATUS);
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
		UserSession userSession = getUserSession();
		_Session session = new _Session(getAppTemplate(), userSession);
		String url = session.getLoginURL();
		if (userSession != null) {
			request.getSession(false).removeAttribute(EnvConst.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}

		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, "", "/", null, null, 0, false);
		return Response.status(HttpServletResponse.SC_OK).entity(url).cookie(cookie).build();

	}

	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response signUp(@FormParam("email") String email, @FormParam("pwd") String pwd){
		Outcome res = new Outcome();

		if (!_Validator.checkEmail(email)) {
			return Response.status(HttpServletResponse.SC_OK).entity(res.setError(true).addMessage("email is incorrect")).build();
		}

		if (!_Validator.checkPwdWeakness(pwd, 8)) {
			return Response.status(HttpServletResponse.SC_OK).entity(res.setError(true).addMessage("pwd is weak")).build();
		}


		ISystemDatabase sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase();
		User userExists = sdb.getUser(email);
		if (userExists != null) {
			Server.logger.verboseLogEntry("User \"" + email + "\" is exist");
			return Response.status(HttpServletResponse.SC_OK).entity(res.setError(true).addMessage("user is exists")).build();
		}

		_Session session = getSession();
		com.flabser.users.User user = session.getAppUser();
		user.setLogin(email);
		try {
			user.setPwd(pwd);
			user.setEmail(email);
		} catch (WebFormValueException e) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(e)).build();
		}

		user.setStatus(UserStatusType.NOT_VERIFIED);
		user.setRegDate(new Date());
		user.setVerifyCode(_Helper.getRandomValue());

		if (!user.save()) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(true).addMessage("user save error")).build();
		}

		VerifyEMail sve = new VerifyEMail(session, user);
		if (sve.send()) {
			user.setStatus(UserStatusType.WAITING_FOR_VERIFYCODE);
			if (!user.save()) {
				return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(true).addMessage("user save error")).build();
			}
		} else {
			return Response.status(HttpServletResponse.SC_OK).entity(res.setError(true).addMessage("verify email sending error")).build();

		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}

	@POST
	@Path("/resetpassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetPassword(@FormParam("email") String email){
		User user = DatabaseFactory.getSysDatabase().getUser(email);
		Outcome res = new Outcome();
		if (user != null) {
			if (user.getStatus() == UserStatusType.REGISTERED) {
				try {
					user.setPwd(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 10));
					if (user.save()){
						_Session session = getSession();
						ResetPasswordEMail sve = new ResetPasswordEMail(session, user);
						if (sve.send()) {
							user.setStatus(UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD);
							if (!user.save()) {
								res.setError(true);
								res.addMessage("user has not saved");
							}
						} else {
							user.setStatus(UserStatusType.RESET_PASSWORD_NOT_SENT);
							user.save();
							res.setError(true);
							res.addMessage("reset password has been not sent");
						}
					}
				} catch (WebFormValueException e) {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(e)).build();
				}
			} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD) {
				res.setError(true);
				res.addMessage("reset password alrady sent");
			} else {
				res.setError(true);
				res.addMessage("unknow user status");
			}
		} else {
			res.setError(true);
			res.addMessage("the user has not found");
		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}
}
