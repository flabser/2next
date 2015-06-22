package cashtracker.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("transactions")
public class TransactionService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TransactionsResponse get() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		return new TransactionsResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Transaction get(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		Transaction m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Transaction create(Transaction m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addTransaction(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Transaction update(@PathParam("id") long id, Transaction m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		dao.updateTransaction(m);
		return m;
	}

	@JsonRootName("transactions")
	class TransactionsResponse extends ArrayList <Transaction> {

		private static final long serialVersionUID = 1L;

		public TransactionsResponse(Collection <? extends Transaction> m) {
			addAll(m);
		}
	}
}
