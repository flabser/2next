package com.flabser.restful;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.users.UserSession;
import com.flabser.users.UserSession.ActiveApplication;

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

}
