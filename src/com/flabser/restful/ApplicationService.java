package com.flabser.restful;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.restful.pojo.Outcome;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.ActiveApplication;
import com.flabser.users.VisibiltyType;

@Path("/application")
public class ApplicationService extends RestProvider {


	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles() {
		UserSession userSession = getUserSession();
		ActiveApplication aa = userSession.getActiveApplication(getAppTemplate().templateType);
		ApplicationProfile ap = aa.getParent();
		return Response.status(HttpServletResponse.SC_OK).entity(ap.getRoles()).build();
	}

	@POST
	@Path("/regapp")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response regApp(@FormParam("apptype") String appType, @FormParam("appname") String appName, @FormParam("visibilty") String visibilty, @FormParam("description") String description){
		Outcome res = new Outcome();
		VisibiltyType vis = VisibiltyType.ONLY_MEMBERS;
		User user = getUserSession().currentUser;

		if (visibilty.equals("public")) {
			vis = VisibiltyType.PUBLIC;
		}

		if (appType != null && 	Environment.availableTemplates.containsKey(appType)) {
			ApplicationProfile ap = new ApplicationProfile();
			ap.appType = appType;
			ap.appName = appName;
			ap.setVisibilty(vis);
			ap.setDesciption(description);
			ap.owner = user.getLogin();
			ap.dbName = ap.appType.toLowerCase() + ap.appId();
			ap.setStatus(ApplicationStatusType.READY_TO_DEPLOY);
			if (ap.save()) {
				user.addApplication(ap);
				if (user.save()) {
					return Response.status(HttpServletResponse.SC_OK).entity(res.addMessage(ap.getStatus().name())).build();
				} else {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(true).addMessage("user save error")).build();
				}
			} else {
				return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(true).addMessage("application save error")).build();
			}
		}else{
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(res.setError(true).addMessage("unknown template")).build();
		}
	}

}
