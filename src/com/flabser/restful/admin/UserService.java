package com.flabser.restful.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.IUser;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.LanguageType;
import com.flabser.restful.RestProvider;
import com.flabser.restful.pojo.Outcome;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._Session;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;

@Path("/users")
public class UserService extends RestProvider {
	private int pageSize = 30;
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UsersList get() {
		// System.out.println("get users");
		_Session ses = getSession();
		if (ses != null) {
			ArrayList<IUser> users = sysDatabase.getAllUsers("", RuntimeObjUtil.calcStartEntry(1, pageSize), pageSize);
			return new UsersList(users);
		} else {
			return null;
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		User user = sysDatabase.getUser(id);
		return Response.ok(user).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User user) throws ClassNotFoundException, SQLException, InstantiationException,
			DatabasePoolException, IllegalAccessException {
		user.setRegDate(new Date());
		user.setStatus(UserStatusType.REGISTERED);
		user.setVerifyCode("");
		user.save();
		return Response.ok(user).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, User u) {
		Outcome res = new Outcome();
		User user = sysDatabase.getUser(id);
		try {
			user.refresh(u);
			user.save();
		} catch (WebFormValueException e) {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST)
					.entity(res.setMessage(e, LanguageType.ENG.name())).build();
		}
		return Response.ok(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		int res = sysDatabase.deleteUser(id);
		if (res == 1) {
			return Response.status(HttpServletResponse.SC_OK).build();
		} else {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		}
	}

	@JsonRootName("users")
	class UsersList extends ArrayList<IUser> {
		private static final long serialVersionUID = -9012621540375574267L;

		public UsersList(Collection<? extends IUser> m) {
			addAll(m);
		}
	}

}
