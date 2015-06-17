package com.flabser.solutions.cashtracker.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cashtracker.dao.TransactionDAO;
import cashtracker.model.Transaction;

import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("/transactions")
public class TransactionService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransactions() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		List <Transaction> transactions = dao.findAll();
		Map <String, List <Transaction>> map = new HashMap <String, List <Transaction>>();
		map.put("transactions", transactions);

		return Response.ok(map).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransaction(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		Transaction transaction = dao.findById(id);

		// Map <String, Object> result = new HashMap <String, Object>();
		// result.put("transaction", transaction);
		// result.put("userProfile", userSession.currentUser);

		Response response = Response.ok(transaction).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addTransaction(Transaction transaction) {
		System.out.println(transaction);

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TransactionDAO dao = new TransactionDAO(new _Session(getAppEnv(), userSession));
		transaction.setId(dao.addTransaction(transaction));

		Response response = Response.ok(transaction).build();

		return response;
	}
}
