package com.flabser.solutions.cashtracker.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
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

import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("/accounts")
public class AccountService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		List <Account> result = dao.findAll();
		Map <String, List <Account>> map = new HashMap <String, List <Account>>();
		map.put("accounts", result);

		return Response.ok(map).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		Account m = dao.findById(id);

		Response response = Response.ok(m).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Account m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addAccount(m));

		Response response = Response.ok(m).build();

		return response;
	}

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Account m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		AccountDAO dao = new AccountDAO(new _Session(getAppEnv(), userSession));
		dao.updateAccount(m);

		Response response = Response.ok(m).build();

		return response;
	}
}
