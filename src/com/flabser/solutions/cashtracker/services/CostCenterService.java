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

import cashtracker.dao.CostCenterDAO;
import cashtracker.model.CostCenter;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.script._Session;
import com.flabser.users.UserSession;


@Path("costcenters")
public class CostCenterService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CostCentersResponse get() {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		return new CostCentersResponse(dao.findAll());
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public CostCenter get(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		CostCenter m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CostCenter create(CostCenter m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addCostCenter(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CostCenter update(@PathParam("id") long id, CostCenter m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		CostCenterDAO dao = new CostCenterDAO(new _Session(getAppEnv(), userSession));
		dao.updateCostCenter(m);
		return m;
	}

	@JsonRootName("costCenters")
	class CostCentersResponse extends ArrayList <CostCenter> {

		private static final long serialVersionUID = 1L;

		public CostCentersResponse(Collection <? extends CostCenter> m) {
			addAll(m);
		}
	}
}
