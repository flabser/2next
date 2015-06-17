package com.flabser.solutions.cashtracker.services;

import java.util.List;

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

		return Response.ok(result).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") int id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		CostCenter cc = dao.findById(id);

		Response response = Response.ok(cc).build();

		return response;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(CostCenter cc) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		cc.setId(dao.addCostCenter(cc));

		Response response = Response.ok(cc).build();

		return response;
	}

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, CostCenter cc) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		dao.updateCostCenter(cc);

		Response response = Response.ok(cc).build();

		return response;

	}
}
