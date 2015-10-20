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
import cashtracker.pojo.Errors;
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
	public Response create(Category m) {
		CategoryDAO dao = new CategoryDAO(getSession());

		// parent category
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
	public Response update(@PathParam("id") long id, Category m) {
		CategoryDAO dao = new CategoryDAO(getSession());

		m.setId(id);

		// parent category
		if (m.getParent() != null) {
			m.setParent(dao.findById(m.getParent().getId()));
		}

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		Category pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		pm.setName(m.getName());
		pm.setParent(m.getParent());
		pm.setTransactionTypes(m.getTransactionTypes());
		pm.setNote(m.getNote());
		pm.setColor(m.getColor());
		pm.setEnabled(m.isEnabled());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		CategoryDAO dao = new CategoryDAO(getSession());
		Category m = dao.findById(id);
		if (m != null) {
			if (dao.existsTransactionByCategory(m)) {
				Errors msg = new Errors();
				msg.setMessage("exists_transaction");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else if (dao.existsChildCategory(m)) {
				Errors msg = new Errors();
				msg.setMessage("exists_child");
				return Response.status(Status.BAD_REQUEST).entity(msg).build();
			} else {
				dao.delete(m);
			}
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@JsonRootName("categories")
	class Categories extends ArrayList <Category> {

		private static final long serialVersionUID = 1L;

		public Categories(Collection <? extends Category> m) {
			addAll(m);
		}
	}
}
