package cashtracker.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.BudgetDAO;
import cashtracker.model.Budget;
import cashtracker.validation.BudgetValidator;
import cashtracker.validation.ValidationError;

import com.flabser.restful.RestProvider;
import com.flabser.restful.data.IAppEntity;


@Path("budget")
public class BudgetService extends RestProvider {

	private BudgetValidator validator = new BudgetValidator();

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		BudgetDAO dao = new BudgetDAO(getSession());
		List <IAppEntity> budgets = dao.findAll();
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
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		BudgetDAO dao = new BudgetDAO(getSession());

		List <IAppEntity> budgets = dao.findAll();
		if (budgets.size() > 0) {
			// return exists
			return Response.ok((Budget) budgets.get(0)).build();
		}

		dao.add(m);
		return Response.ok(m).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Budget m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.ok(ve).status(Status.BAD_REQUEST).build();
		}

		BudgetDAO dao = new BudgetDAO(getSession());
		m.setId((long) 1);
		dao.update(m);
		return Response.ok(m).build();
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
