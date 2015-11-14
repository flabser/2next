package task.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;

import task.dao.IssueDAO;
import task.dao.filter.IssueFilter;
import task.helper.IssueFilterBuilder;
import task.helper.PageRequest;
import task.model.Issue;
import task.pojo.Meta;
import task.validation.IssueValidator;
import task.validation.ValidationError;


@Path("issues")
public class IssueService extends RestProvider {

	private IssueValidator validator = new IssueValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("at") String at, @QueryParam("st") String status,
			@QueryParam("tags") List <Long> tagIds, @QueryParam("u") List <Long> assignees,
			@QueryParam("offset") int offset, @QueryParam("limit") int limit, @QueryParam("sort") String sort) {

		// UNAUTHORIZED
		if (!getSession().getUser().isAuthorized) {
			return Response.noContent().status(Status.UNAUTHORIZED).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		IssueFilter filter = IssueFilterBuilder.create(getSession().getUser(), status, tagIds, assignees, at);
		PageRequest pageRequest = new PageRequest(offset, limit, sort);

		int count = dao.getCount().intValue();
		List <Issue> list = dao.find(filter, pageRequest);
		_Response _resp = new _Response(list, new Meta(count, pageRequest.getLimit(), pageRequest.getOffset(), 0));

		ObjectMapper om = new ObjectMapper();
		om.disable(SerializationFeature.WRAP_ROOT_VALUE);

		try {
			return Response.ok(om.writeValueAsString(_resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return Response.noContent().status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {

		// UNAUTHORIZED
		if (!getSession().getUser().isAuthorized) {
			return Response.noContent().status(Status.UNAUTHORIZED).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		Issue m = dao.findById(id);
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}

		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Issue m) {

		// UNAUTHORIZED
		if (!getSession().getUser().isAuthorized) {
			return Response.noContent().status(Status.UNAUTHORIZED).build();
		}

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Issue m) {

		// UNAUTHORIZED
		if (!getSession().getUser().isAuthorized) {
			return Response.noContent().status(Status.UNAUTHORIZED).build();
		}

		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		Issue pm = dao.findById(id);
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}

		pm.setBody(m.getBody());
		pm.setPriority(m.getPriority());
		pm.setMilestone(m.getMilestone());
		pm.setTags(m.getTags());
		pm.setAssignee(m.getAssignee());

		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {

		// UNAUTHORIZED
		if (!getSession().getUser().isAuthorized) {
			return Response.noContent().status(Status.UNAUTHORIZED).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		Issue m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.noContent().build();
	}

	class _Response {

		public List <Issue> issues;
		public Meta meta;

		public _Response(List <Issue> list, Meta meta) {
			this.issues = list;
			this.meta = meta;
		}
	}
}
