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

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("accounts")
public class AccountService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccountsResponse get() {
		AccountDAO dao = new AccountDAO(getSession());
		return new AccountsResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Account get(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Account create(Account m) {
		AccountDAO dao = new AccountDAO(getSession());
		m.setId(dao.addAccount(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Account update(@PathParam("id") long id, Account m) {

		m.setId(id);
		AccountDAO dao = new AccountDAO(getSession());
		dao.updateAccount(m);
		return m;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
		if (m != null) {
			dao.deleteAccount(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("accounts")
	class AccountsResponse extends ArrayList <Account> {

		private static final long serialVersionUID = 1L;

		public AccountsResponse(Collection <? extends Account> m) {
			addAll(m);
		}
	}
}
