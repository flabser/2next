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

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("/costcenters")
public class CostCenterService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		List <CostCenter> result = dao.findAll();
		Map <String, List <CostCenter>> map = new HashMap <String, List <CostCenter>>();
		map.put("costCenters", result);

		return Response.ok(map).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		CostCenter m = dao.findById(id);

		Response response = Response.ok(m).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(CostCenter m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addCostCenter(m));

		Response response = Response.ok(m).build();

		return response;
	}

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, CostCenter m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		dao.updateCostCenter(m);

		Response response = Response.ok(m).build();

		return response;
	}
}
