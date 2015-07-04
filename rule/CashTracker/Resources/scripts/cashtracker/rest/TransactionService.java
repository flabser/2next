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

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("transactions")
public class TransactionService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TransactionsResponse get() {
		TransactionDAO dao = new TransactionDAO(getSession());
		return new TransactionsResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Transaction get(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Transaction create(Transaction m) {
		TransactionDAO dao = new TransactionDAO(getSession());
		m.setId(dao.addTransaction(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Transaction update(@PathParam("id") long id, Transaction m) {

		m.setId(id);
		TransactionDAO dao = new TransactionDAO(getSession());
		dao.updateTransaction(m);
		return m;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		TransactionDAO dao = new TransactionDAO(getSession());
		Transaction m = dao.findById(id);
		if (m != null) {
			dao.deleteTransaction(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("transactions")
	class TransactionsResponse extends ArrayList <Transaction> {

		private static final long serialVersionUID = 1L;

		public TransactionsResponse(Collection <? extends Transaction> m) {
			addAll(m);
		}
	}
}
