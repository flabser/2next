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

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("accounts")
public class AccountService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AccountsResponse get() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		return new AccountsResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Account get(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		Account m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Account create(Account m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addAccount(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Account update(@PathParam("id") long id, Account m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		dao.updateAccount(m);
		return m;
	}

	@JsonRootName("accounts")
	class AccountsResponse extends ArrayList <Account> {

		private static final long serialVersionUID = 1L;

		public AccountsResponse(Collection <? extends Account> m) {
			addAll(m);
		}
	}
}
