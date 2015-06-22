package com.flabser.solutions.cashtracker.services;

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

import cashtracker.dao.CategoryDAO;
import cashtracker.model.Category;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("categories")
public class CategoryService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CategoriesResponse get() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		return new CategoriesResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Category get(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		Category m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category create(Category m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addCategory(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Category update(@PathParam("id") int id, Category m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		CategoryDAO dao = new CategoryDAO(new _Session(getAppEnv(), userSession));
		dao.updateCategory(m);
		return m;
	}

	@JsonRootName("categories")
	class CategoriesResponse extends ArrayList <Category> {

		private static final long serialVersionUID = 1L;

		public CategoriesResponse(Collection <? extends Category> m) {
			addAll(m);
		}
	}
}
