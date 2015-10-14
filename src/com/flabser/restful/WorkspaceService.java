package com.flabser.restful;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/workspace")
public class WorkspaceService  extends RestProvider  {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {

		return Response.status(HttpServletResponse.SC_OK).build();
	}

	@GET
	@Path("/url")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getURL() throws URISyntaxException {
		String redirectURL = getSession().getWorkspaceURL();
		return Response.seeOther(new URI(redirectURL)).build();
	}


}
