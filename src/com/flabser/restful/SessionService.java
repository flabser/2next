package com.flabser.restful;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.omg.CORBA.UserException;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.Invitation;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.env.Site;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.AuthFailedExceptionType;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.ServerServiceWarningType;
import com.flabser.exception.WebFormValueException;
import com.flabser.restful.pojo.AppUser;
import com.flabser.restful.pojo.Outcome;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.script._Helper;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.server.Server;
import com.flabser.servlets.ServletUtil;
import com.flabser.users.AuthModeType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.users._UsersHelper;
import com.flabser.util.Util;

import nubis.page.app.InvitationEMail;
import nubis.page.app.ResetPasswordEMail;
import nubis.page.app.VerifyEMail;

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
			String fileName = user.getAvatar().getRealFileName();
			if (!fileName.equals("")) {
				fn = userTmpDir.getAbsolutePath() + File.separator + fileName;
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
	DatabasePoolException, UserException, IllegalAccessException, SQLException, URISyntaxException {
		UserSession userSession = null;
		HttpSession jses;
		// String appID = authUser.getAppId();
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

		Server.logger.normalLogEntry(userID + " has connected (" + context.getContextPath() + ")");
		IActivity ua = DatabaseFactory.getSysDatabase().getActivity();
		ua.postLogin(ServletUtil.getClientIpAddr(request), user);
		userSession = new UserSession(user);

		if (user.getStatus() == UserStatusType.REGISTERED) {
			authUser = userSession.getUserPOJO();
			// authUser.setAppId(appID);
		} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_INVITATION
				|| user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD) {
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response destroySession() throws URISyntaxException {
		UserSession userSession = getUserSession();
		AppTemplate env = getAppTemplate();
		String url = "";
		if (userSession.getAuthMode() == AuthModeType.DIRECT_LOGIN) {
			url = env.getHostName() + "/" + env.templateType + "/" + getAppID() + "/Provider?id=login";
			;
		} else {
			Site site = Environment.availableTemplates.get(Environment.getWorkspaceName());
			url = site.getAppTemlate().getHostName() + "/Provider?id=login";
		}
		Outcome res = new Outcome();
		res.addMessage(url);
		if (userSession != null) {
			request.getSession(false).removeAttribute(EnvConst.SESSION_ATTR);
			SessionPool.remove(userSession);
			userSession = null;
			// jses.invalidate();
		}

		NewCookie cookie = new NewCookie(EnvConst.AUTH_COOKIE_NAME, "", "/", null, null, 0, false);
		// return Response.seeOther(new URI(url)).cookie(cookie).build();
		return Response.status(HttpServletResponse.SC_OK).entity(res).cookie(cookie).build();

	}

	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response signUp(@FormParam("email") String email, @FormParam("pwd") String pwd) {
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLang();
		if (!_Validator.checkEmail(email)) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.EMAIL_IS_INCORRECT, lang)).build();
		}

		if (!_Validator.checkPwdWeakness(pwd, 8)) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.WEAK_PASSWORD, lang)).build();
		}

		ISystemDatabase sdb = com.flabser.dataengine.DatabaseFactory.getSysDatabase();
		User userExists = sdb.getUser(email);
		if (userExists != null) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.USER_EXISTS, lang)).build();
		}

		com.flabser.users.User user = session.getAppUser();
		user.setLogin(email);
		try {
			user.setPwd(pwd);
			user.setEmail(email);
		} catch (WebFormValueException e) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(e, lang)).build();
		}

		user.setStatus(UserStatusType.NOT_VERIFIED);
		user.setRegDate(new Date());
		user.setVerifyCode(_Helper.getRandomValue());

		if (!user.save()) {
			return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
					.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
		}

		VerifyEMail sve = new VerifyEMail(session, user);
		if (sve.send()) {
			user.setStatus(UserStatusType.WAITING_FOR_VERIFYCODE);
			if (!user.save()) {
				return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
						.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
			}
		} else {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceWarningType.VERIFY_EMAIL_SENDING_ERROR, lang)).build();

		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}

	@POST
	@Path("/resetpassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response resetPassword(@FormParam("email") String email) {
		User user = DatabaseFactory.getSysDatabase().getUser(email);
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLang();

		if (user != null) {
			if (user.getStatus() == UserStatusType.REGISTERED) {
				try {
					user.setPwd(Util.generateRandomAsText("qwertyuiopasdfghjklzxcvbnm1234567890", 10));
					if (user.save()) {
						ResetPasswordEMail sve = new ResetPasswordEMail(session, user);
						if (sve.send()) {
							user.setStatus(UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD);
							if (!user.save()) {
								return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
										.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
							}
						} else {
							user.setStatus(UserStatusType.RESET_PASSWORD_NOT_SENT);
							user.save();
							return Response.status(HttpServletResponse.SC_OK)
									.entity(res.setMessage(ServerServiceWarningType.RESET_PASSWORD_SENDING_ERROR, lang))
									.build();
						}
					}
				} catch (WebFormValueException e) {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(e, lang)).build();
				}
			} else if (user.getStatus() == UserStatusType.WAITING_FIRST_ENTERING_AFTER_RESET_PASSWORD) {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceWarningType.RESET_PASSWORD_ALREADY_SENT, lang)).build();
			} else {
				return Response.status(HttpServletResponse.SC_OK)
						.entity(res.setMessage(ServerServiceWarningType.UNKNOWN_USER_STATUS, lang)).build();
			}
		} else {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(res.setMessage(ServerServiceExceptionType.USER_NOT_FOUND, lang)).build();
		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}

	@POST
	@Path("/invite")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response invite(Invitation ivitation) {
		Outcome result = new Outcome();
		_Session session = getSession();
		String lang = session.getLang();

		if (!_Validator.checkEmail(ivitation.getEmail())) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(result.setMessage(ServerServiceExceptionType.EMAIL_IS_INCORRECT, lang)).build();
		}

		if (ivitation.getMessage() == null || ivitation.getMessage().isEmpty()) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(result.setMessage(ServerServiceExceptionType.VALUE_IS_EMPTY, lang)).build();
		}

		User user = getUserSession().currentUser;
		ApplicationProfile ap = user.getApplicationProfile(getAppID());
		User tempUser = _UsersHelper.regTempApplicationUser(ap, ivitation.getEmail());
		if (tempUser != null) {
			InvitationEMail sve = new InvitationEMail(session, user, ivitation.getMessage(), tempUser);
			if (sve.send()) {
				tempUser.setStatus(UserStatusType.WAITING_FIRST_ENTERING_AFTER_INVITATION);
				if (!tempUser.save()) {
					return Response.status(Status.BAD_REQUEST)
							.entity(result.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
				}
			} else {
				tempUser.setStatus(UserStatusType.INVITATTION_NOT_SENT);
				tempUser.save();
				return Response.status(HttpServletResponse.SC_OK)
						.entity(result.setMessage(ServerServiceWarningType.INVITATION_SENDING_ERROR, lang)).build();
			}
			ivitation.setAppType(ap.appType);
			ivitation.setAppID(ap.appID);
			ivitation.setTempLogin(tempUser.id);
			ivitation.setAuthor(user.id);
			if (ivitation.save()) {
				return Response.status(Status.OK).entity(tempUser).build();
			} else {
				return Response.status(Status.BAD_REQUEST)
						.entity(result.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
			}

		} else {
			return Response.status(Status.BAD_REQUEST)
					.entity(result.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
		}
	}
}
