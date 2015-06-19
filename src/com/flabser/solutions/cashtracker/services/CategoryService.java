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

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("/categories")
public class CategoryService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		List <Category> result = dao.findAll();
		Map <String, List <Category>> map = new HashMap <String, List <Category>>();
		map.put("categories", result);

		return Response.ok(map).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		Category m = dao.findById(id);

		Response response = Response.ok(m).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Category m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addCategory(m));

		Response response = Response.ok(m).build();

		return response;
	}

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Category m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		dao.updateCategory(m);

		Response response = Response.ok(m).build();

		return response;
	}
}
