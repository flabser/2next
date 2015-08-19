package cashtracker.rest;

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

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;
import cashtracker.validation.TagValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.restful.data.IAppEntity;


@Path("tags")
public class TagService extends RestProvider {

	private TagValidator validator = new TagValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		TagDAO dao = new TagDAO(getSession());
		return Response.ok(new Tags(dao.findAll())).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		IAppEntity m = dao.findById(id);
		System.out.println(((Tag) m).getName());
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tag m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		TagDAO dao = new TagDAO(getSession());
		dao.add(m);
		return Response.ok(m).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Tag m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		TagDAO dao = new TagDAO(getSession());
		dao.update(m);
		return Response.ok(m).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TagDAO dao = new TagDAO(getSession());
		Tag m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("tags")
	class Tags extends ArrayList <IAppEntity> {

		private static final long serialVersionUID = 1L;

		public Tags(Collection <? extends IAppEntity> m) {
			addAll(m);
		}
	}
}
