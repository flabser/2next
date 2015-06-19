package com.flabser.solutions.cashtracker.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("/tags")
public class TagService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		List <Tag> result = dao.findAll();
		Map <String, List <Tag>> map = new HashMap <String, List <Tag>>();
		map.put("tags", result);

		return Response.ok(map).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		Tag m = dao.findById(id);
		Map <String, Tag> map = new HashMap <String, Tag>();
		map.put("tag", m);

		Response response = Response.ok(map).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addTag(m));

		Response response = Response.ok(m).build();

		return response;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		dao.updateTag(m);

		Response response = Response.ok(m).build();

		return response;
	}
}
