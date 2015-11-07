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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;

import task.dao.TagDAO;
import task.model.Tag;
import task.pojo.Errors;
import task.validation.TagValidator;
import task.validation.ValidationError;


@Path("tags")
public class TagService extends RestProvider {

	private TagValidator validator = new TagValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		TagDAO dao = new TagDAO(getSession());
		Tags tags = new Tags(dao.findAll());
		return Response.ok(tags).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}

		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tag m) {
		TagDAO dao = new TagDAO(getSession());

		// parent tag
		if (m.getParent() != null) {
			m.setParent(dao.findById(m.getParent().getId()));
		}

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Tag m) {
		TagDAO dao = new TagDAO(getSession());

		m.setId(id);

		// parent tag
		if (m.getParent() != null) {
			m.setParent(dao.findById(m.getParent().getId()));
		}

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		Tag pm = dao.findById(id);
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}

		pm.setName(m.getName());
		pm.setParent(m.getParent());

		return Response.ok(dao.update(pm)).build();
	}

	@PUT
	@Path("/{id}/set")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setParent(@PathParam("id") long id, @QueryParam("parent") long parentId) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);

		if (parentId > 0) {
			Tag parent = dao.findById(parentId);
			if (parent == null) {
				return Response.noContent().status(Status.NOT_FOUND).build();
			}
			m.setParent(parent);
		} else {
			m.setParent(null);
		}

		return Response.ok(dao.update(m)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		if (m != null) {
			if (dao.existsChildTag(m)) {
				Errors msg = new Errors();
				msg.setMessage("exists_child");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else {
				dao.delete(m);
			}
		}

		return Response.noContent().build();
	}

	@JsonRootName("tags")
	class Tags extends ArrayList <Tag> {

		private static final long serialVersionUID = 1L;

		public Tags(Collection <? extends Tag> m) {
			addAll(m);
		}
	}
}
