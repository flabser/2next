package com.flabser.restful;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.dataengine.system.entities.UserRole;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.exception.ServerServiceExceptionType;
import com.flabser.restful.pojo.Outcome;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;
import com.flabser.users.VisibiltyType;

@Path("/application")
public class ApplicationService extends RestProvider {

	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		Outcome res = new Outcome();
		ApplicationProfile ap = DatabaseFactory.getSysDatabase().getApp(context.getServletContextName());
		for(UserRole role: ap.getRoles()){
			res.addMessage(role.getName());
		}
		return Response.status(HttpServletResponse.SC_OK).entity(res).build();
	}

	@POST
	@Path("/regapp")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response regApp(@FormParam("apptype") String appType, @FormParam("appname") String appName,
			@FormParam("visibilty") String visibilty, @FormParam("description") String description)
					throws ServletException {
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLang();

		VisibiltyType vis = VisibiltyType.ONLY_MEMBERS;
		User user = getUserSession().currentUser;

		if (visibilty.equals("public")) {
			vis = VisibiltyType.PUBLIC;
		}
		Site site = Environment.availableTemplates.get(appType);
		if (site != null) {
			ApplicationProfile ap = new ApplicationProfile();
			ap.appType = appType;
			ap.appName = appName;
			ap.setVisibilty(vis);
			ap.setDesciption(description);
			ap.owner = user.getLogin();
			ap.dbName = ap.appType.toLowerCase() + ap.getAppId();
			ap.setStatus(ApplicationStatusType.READY_TO_DEPLOY);
			if (ap.save()) {
				user.addApplication(ap);
				if (user.save()) {
					Server.webServerInst.addApplication(ap.getAppID(), site);
					return Response.status(HttpServletResponse.SC_OK).entity(res.addMessage(ap.getStatus().name()))
							.build();
				} else {
					return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
							.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
				}
			} else {
				return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
						.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
			}

		} else {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST)
					.entity(res.setMessage(ServerServiceExceptionType.UNKNOWN_APPLICATION_TEMPLATE, lang)).build();
		}
	}

	@POST
	@Path("/unregapp")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response unregApp(@FormParam("app") String appId) {
		Outcome res = new Outcome();
		_Session session = getSession();
		String lang = session.getLang();

		User user = session.getAppUser();
		ApplicationProfile ap = user.getApplicationProfile(appId);

		if (ap != null) {
			if (ap.owner.equals(user.getLogin())) {
				ap.setStatus(ApplicationStatusType.READY_TO_REMOVE);
				if (!ap.save()) {
					return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
							.entity(res.setMessage(ServerServiceExceptionType.SERVER_ERROR, lang)).build();
				}
			}
			return Response.status(HttpServletResponse.SC_OK).entity(res.addMessage(ap.getStatus().name())).build();
		}
		return Response.status(HttpServletResponse.SC_OK).build();
	}

}
