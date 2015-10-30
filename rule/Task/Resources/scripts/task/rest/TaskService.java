package task.rest;

import java.util.ArrayList;
import java.util.Collection;

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

import task.dao.TaskDAO;
import task.model.Task;
import task.validation.TaskValidator;
import task.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("task")
public class TaskService extends RestProvider {

	private TaskValidator validator = new TaskValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		TaskDAO dao = new TaskDAO(getSession());
		return Response.ok(new Tasks(dao.findAll())).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TaskDAO dao = new TaskDAO(getSession());
		Task m = dao.findById(id);
		//
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Task m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TaskDAO dao = new TaskDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Task m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TaskDAO dao = new TaskDAO(getSession());
		Task pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		pm.setName(m.getName());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TaskDAO dao = new TaskDAO(getSession());
		Task m = dao.findById(id);
		dao.delete(m);
		return Response.status(Status.NO_CONTENT).build();
	}

	@JsonRootName("tasks")
	class Tasks extends ArrayList <Task> {

		private static final long serialVersionUID = 1L;

		public Tasks(Collection <? extends Task> m) {
			addAll(m);
		}
	}
}
