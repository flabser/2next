package nubis.rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nubis.dao.WorkspaceDAO;
import nubis.model.Workspace;

import com.flabser.restful.RestProvider;

@Path("workspaces")
public class WorkspaceService  extends RestProvider {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		WorkspaceDAO dao = new WorkspaceDAO(getSession());
		Workspace ws = dao.find();
		return Response.status(HttpServletResponse.SC_OK).entity(ws).build();
	}

}
