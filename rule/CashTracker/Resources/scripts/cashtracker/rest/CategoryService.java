package cashtracker.rest;

import java.util.List;

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
import cashtracker.model.Errors;
import cashtracker.validation.CategoryValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flabser.restful.RestProvider;


@Path("categories")
public class CategoryService extends RestProvider {

	private CategoryValidator validator = new CategoryValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		CategoryDAO dao = new CategoryDAO(getSession());
		List <Category> list = dao.findAll();
		_Response resp = new _Response("success", list, new Meta(list.size(), -1, -1));

		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		om.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

		try {
			return Response.ok(om.writeValueAsString(resp)).build();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
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
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Category m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		CategoryDAO dao = new CategoryDAO(getSession());
		//
		Category pm = dao.findById(id);
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

	class Meta {

		public int total = 0;
		public int limit = 20;
		public int offset = 0;

		public Meta(int total, int limit, int offset) {
			this.total = total;
			this.limit = limit;
			this.offset = offset;
		}
	}

	class _Response {

		public String status;
		public List <Category> categories;
		public Meta meta;

		public _Response(String status, List <Category> list, Meta meta) {
			this.status = status;
			this.categories = list;
			this.meta = meta;
		}
	}
}
