package com.flabser.restful.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.Invitation;
import com.flabser.dataengine.system.entities.constants.InvitationStatusType;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.exception.ServerServiceWarningType;
import com.flabser.mail.message.IEMail;
import com.flabser.mail.message.InvitationEMail;
import com.flabser.mail.message.InvitationEMailExist;
import com.flabser.restful.RestProvider;
import com.flabser.restful.pojo.Outcome;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._Session;
import com.flabser.script._Validator;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;
import com.flabser.users._UsersHelper;

@Path("/invitations")
public class InvService extends RestProvider {
	private int pageSize = 30;
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AppsList get() {
		// System.out.println("get users");
		_Session ses = getSession();
		if (ses != null) {
			ArrayList<ApplicationProfile> apps = sysDatabase.getAllApps("", RuntimeObjUtil.calcStartEntry(1, pageSize),
					pageSize);
			return new AppsList(apps);
		} else {
			return null;
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		System.out.println("GET " + id);
		User user = sysDatabase.getUser(id);
		return Response.ok(user).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Invitation invitation) throws ClassNotFoundException, SQLException, InstantiationException,
			DatabasePoolException, IllegalAccessException {
		IEMail message = null;
		Outcome result = new Outcome();
		_Session session = getSession();
		String lang = session.getLanguage();

		if (!_Validator.checkEmail(invitation.getEmail())) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(result.setMessage(ServerServiceExceptionType.EMAIL_IS_INCORRECT, lang)).build();
		}

		if (invitation.getMessage() == null || invitation.getMessage().isEmpty()) {
			return Response.status(HttpServletResponse.SC_OK)
					.entity(result.setMessage(ServerServiceExceptionType.VALUE_IS_EMPTY, lang)).build();
		}
		User tempUser = DatabaseFactory.getSysDatabase().getUser(invitation.getEmail());
		User user = getSession().getCurrentUser();
		ApplicationProfile ap = user.getApplicationProfile(getAppID());
		if (tempUser == null) {
			tempUser = _UsersHelper.regTempApplicationUser(ap, invitation.getEmail());
			message = new InvitationEMail(session, user, invitation.getMessage(), tempUser);
		} else {
			message = new InvitationEMailExist(session, user, invitation.getMessage(), tempUser);
		}

		if (message.send()) {
			tempUser.setStatus(UserStatusType.WAITING_FIRST_ENTERING);
			if (!tempUser.save()) {
				return Response.status(Status.BAD_REQUEST)
						.entity(result.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
			}
		} else {
			invitation.setStatus(InvitationStatusType.SAVING_FAILED);
			invitation.save();
			return Response.status(HttpServletResponse.SC_OK)
					.entity(result.setMessage(ServerServiceWarningType.INVITATION_SENDING_ERROR, lang)).build();
		}
		invitation.setAppType(ap.appType);
		invitation.setAppID(ap.appID);
		invitation.setTempLogin(tempUser.id);
		invitation.setAuthor(user.id);
		invitation.setStatus(InvitationStatusType.WAITING);
		if (invitation.save()) {
			return Response.status(Status.OK).entity(invitation).build();
		} else {
			return Response.status(Status.BAD_REQUEST)
					.entity(result.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
		}

	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, User u) {
		System.out.println("PUT " + u);
		User user = sysDatabase.getUser(id);
		return Response.ok(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {

		return Response.ok().build();
	}

	@JsonRootName("apps")
	class AppsList extends ArrayList<ApplicationProfile> {
		private static final long serialVersionUID = 1900083586619621666L;

		public AppsList(Collection<? extends ApplicationProfile> m) {
			addAll(m);
		}
	}

}
