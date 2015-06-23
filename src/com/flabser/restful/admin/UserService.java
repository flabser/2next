package com.flabser.restful.admin;

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

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.restful.Container;
import com.flabser.restful.JavaToJSON;
import com.flabser.restful.RestProvider;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._Session;
import com.flabser.servlets.admin.UserServices;
import com.flabser.users.User;
import com.flabser.users.UserSession;

@Path("/users")
public class UserService extends RestProvider {
	private int pageSize = 30;
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UsersList get() {

		_Session ses = getSession();
		if (ses != null) {
			ArrayList users = sysDatabase.getAllUsers("", RuntimeObjUtil.calcStartEntry(1, pageSize), pageSize);
			return new UsersList(users);
		} else {
			return null;
		}
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tag get(@PathParam("id") long id) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		Tag m = dao.findById(id);
		return m;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag create(Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		m.setId(dao.addTag(m));
		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tag update(@PathParam("id") long id, Tag m) {

		HttpSession jses = request.getSession(true);
		UserSession userSession = (UserSession) jses.getAttribute("usersession");

		m.setId(id);
		TagDAO dao = new TagDAO(new _Session(getAppEnv(), userSession));
		dao.updateTag(m);
		return m;
	}

	@JsonRootName("users")
	class UsersList extends ArrayList<User> {

		public UsersList(Collection<? extends User> m) {
			addAll(m);
		}
	}

	public Object produceGet(@PathParam("data_type") String type) {
		String model = ";";
		System.out.println(request.getRequestedSessionId() + " " + type);
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		UserServices us = new UserServices();

		int count = sysDatabase.getAllUsersCount("");
		int pageSize = 100;
		ArrayList<User> fl = sysDatabase.getAllUsers("", RuntimeObjUtil.calcStartEntry(1, pageSize), pageSize);

		Container c = new Container(fl);

		new JavaToJSON(c);
		return c;

	}

}
