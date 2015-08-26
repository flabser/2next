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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cashtracker.dao.TransactionDAO;
import cashtracker.helper.PageRequest;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionType;
import cashtracker.validation.TransactionValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.restful.data.IAppEntity;


@Path("transactions")
public class TransactionService extends RestProvider {

	private TransactionValidator validator = new TransactionValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("offset") int offset, @QueryParam("limit") int limit,
			@QueryParam("order_by") String orderBy, @QueryParam("direction") String direction,
			@QueryParam("type") String trType) {

		PageRequest pr = new PageRequest(offset, limit, orderBy, direction);
		TransactionDAO dao = new TransactionDAO(getSession());
		TransactionType type = null;
		if (trType != null && !trType.isEmpty()) {
			type = TransactionType.typeOf(trType.substring(0, 1).toUpperCase());
		}
		Collection <IAppEntity> list = dao.findAll(pr, type);
		return Response.ok(new Transactions(list)).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Transaction m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TransactionDAO dao = new TransactionDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Transaction m) {
		m.setId(id);
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		TransactionDAO dao = new TransactionDAO(getSession());
		return Response.ok(dao.update(m)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.status(Status.NO_CONTENT).build();
	}

	@JsonRootName("transactions")
	class Transactions extends ArrayList <IAppEntity> {

		private static final long serialVersionUID = 1L;

		public Transactions(Collection <? extends IAppEntity> m) {
			addAll(m);
		}
	}
}
