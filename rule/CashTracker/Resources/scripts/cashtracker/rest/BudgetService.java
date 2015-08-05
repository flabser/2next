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

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.validation.BudgetValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("budgets")
public class BudgetService extends RestProvider {

	private BudgetValidator validator = new BudgetValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		BudgetDAO dao = new BudgetDAO(getSession());
		return Response.ok(new Budgets(dao.findAll())).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		BudgetDAO dao = new BudgetDAO(getSession());
		Budget m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Budget m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		BudgetDAO dao = new BudgetDAO(getSession());
		m.setId(dao.add(m));
		return Response.ok(m).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Budget m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		m.setId(id);
		BudgetDAO dao = new BudgetDAO(getSession());
		dao.update(m);
		return Response.ok(m).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		BudgetDAO dao = new BudgetDAO(getSession());
		Budget m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("budgets")
	class Budgets extends ArrayList <Budget> {

		private static final long serialVersionUID = 1L;

		public Budgets(Collection <? extends Budget> m) {
			addAll(m);
		}
	}
}
