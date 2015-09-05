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

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.validation.BudgetValidator;
import cashtracker.validation.ValidationError;

import com.flabser.restful.RestProvider;


@Path("budget")
public class BudgetService extends RestProvider {

	private BudgetValidator validator = new BudgetValidator();

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		BudgetDAO dao = new BudgetDAO(getSession());
		List <Budget> budgets = dao.findAll();
		if (budgets.size() > 0) {
			return Response.ok(budgets.get(0)).build();
		}
		Budget b = new Budget();
		return Response.ok(b).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Budget m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		BudgetDAO dao = new BudgetDAO(getSession());

		List <Budget> budgets = dao.findAll();
		if (budgets.size() > 0) {
			// return exists
			return Response.ok(budgets.get(0)).build();
		}

		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Budget m) {
		m.setId(id);
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		BudgetDAO dao = new BudgetDAO(getSession());
		//
		List <Budget> budgets = dao.findAll();
		Budget pm;
		if (budgets.size() > 0) {
			pm = budgets.get(0);
			pm.setName(m.getName());
			pm = (Budget) dao.update(pm);
		} else {
			pm = new Budget();
			pm.setName(m.getName());
			pm = (Budget) dao.add(pm);
		}

		//
		return Response.ok(pm).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete() {
		BudgetDAO dao = new BudgetDAO(getSession());
		dao.delete();
		return Response.ok().build();
	}
}
