package com.flabser.restful;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/workspace")
public class WorkspaceService  extends RestProvider  {

	@GET
	@Path("/url")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getURL() {
		return Response.status(HttpServletResponse.SC_OK).entity(getSession().getWorkspaceURL()).build();
	}
}
