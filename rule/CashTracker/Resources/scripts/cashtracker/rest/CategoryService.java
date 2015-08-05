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

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;
import cashtracker.validation.CategoryValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("categories")
public class CategoryService extends RestProvider {

	private CategoryValidator validator = new CategoryValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		CategoryDAO dao = new CategoryDAO(getSession());
		return Response.ok(new Categories(dao.findAll())).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		CategoryDAO dao = new CategoryDAO(getSession());
		Category m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Category m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		CategoryDAO dao = new CategoryDAO(getSession());
		m.setId(dao.add(m));
		return Response.ok(m).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Category m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		m.setId(id);
		CategoryDAO dao = new CategoryDAO(getSession());
		dao.update(m);
		return Response.ok(m).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		CategoryDAO dao = new CategoryDAO(getSession());
		Category m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("categories")
	class Categories extends ArrayList <Category> {

		private static final long serialVersionUID = 1L;

		public Categories(Collection <? extends Category> m) {
			addAll(m);
		}
	}
}
