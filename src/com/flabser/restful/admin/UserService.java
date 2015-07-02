package com.flabser.restful.admin;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.restful.RestProvider;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._Session;
import com.flabser.users.User;

@Path("/users")
public class UserService extends RestProvider {
	private int pageSize = 30;
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UsersList get() {
	//	System.out.println("get users");
		_Session ses = getSession();
		if (ses != null) {
			ArrayList users = sysDatabase.getAllUsers("", RuntimeObjUtil.calcStartEntry(1, pageSize), pageSize);
			return new UsersList(users);
		} else {
			return null;
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User get(@PathParam("id") long id) {
		return null;


	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User create(User m) {


		return m;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User update(@PathParam("id") long id, User m) {


		return m;
	}

	@JsonRootName("users")
	class UsersList extends ArrayList<User> {
		public UsersList(Collection<? extends User> m) {
			addAll(m);
		}
	}

}
