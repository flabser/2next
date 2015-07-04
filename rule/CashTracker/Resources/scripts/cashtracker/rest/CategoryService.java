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

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("categories")
public class CategoryService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CategoriesResponse get() {
		CategoryDAO dao = new CategoryDAO(getSession());
		return new CategoriesResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Category get(@PathParam("id") long id) {
		CategoryDAO dao = new CategoryDAO(getSession());
		Category m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category create(Category m) {
		CategoryDAO dao = new CategoryDAO(getSession());
		m.setId(dao.addCategory(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category update(@PathParam("id") long id, Category m) {

		m.setId(id);
		CategoryDAO dao = new CategoryDAO(getSession());
		dao.updateCategory(m);
		return m;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		CategoryDAO dao = new CategoryDAO(getSession());
		Category m = dao.findById(id);
		if (m != null) {
			dao.deleteCategory(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("categories")
	class CategoriesResponse extends ArrayList <Category> {

		private static final long serialVersionUID = 1L;

		public CategoriesResponse(Collection <? extends Category> m) {
			addAll(m);
		}
	}
}
