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
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.restful.RestProvider;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._Session;
import com.flabser.users.User;
import com.flabser.users.UserStatusType;

@Path("/apps")
public class AppService extends RestProvider {
	private int pageSize = 30;
	private ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public AppsList get() {
		_Session ses = getSession();
		if (ses != null) {
			ArrayList<ApplicationProfile> apps = sysDatabase.getAllApps("", RuntimeObjUtil.calcStartEntry(1, pageSize),
					pageSize);
			return new AppsList(apps);
		} else {
			return null;
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") int id) {
		ApplicationProfile app = sysDatabase.getApp(id);
		if (app != null) {
			return Response.ok(app).build();
		} else {
			return Response.status(HttpServletResponse.SC_NOT_FOUND).build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User user) throws ClassNotFoundException, SQLException, InstantiationException,
			DatabasePoolException, IllegalAccessException {
		System.out.println("POST " + user);
		user.setRegDate(new Date());
		user.lastURL = "";
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
		System.out.println("PUT " + u);
		User user = sysDatabase.getUser(id);
		return Response.ok(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		User user = sysDatabase.getUser(id);
		user.delete();
		return Response.ok().build();
	}

	@JsonRootName("apps")
	class AppsList extends ArrayList<ApplicationProfile> {
		private static final long serialVersionUID = -5831473279550384891L;

		public AppsList(Collection<? extends ApplicationProfile> m) {
			addAll(m);
		}
	}

}
