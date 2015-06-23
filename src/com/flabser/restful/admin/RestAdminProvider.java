package com.flabser.restful.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.omg.CORBA.UserException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.activity.IActivity;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.PortalException;
import com.flabser.restful.Container;
import com.flabser.restful.JavaToJSON;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.script._IObject;
import com.flabser.script._Page;
import com.flabser.servlets.Cookies;
import com.flabser.servlets.ProviderExceptionType;
import com.flabser.servlets.PublishAsType;
import com.flabser.servlets.ServletUtil;
import com.flabser.servlets.admin.UserServices;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.users.UserStatusType;
import com.flabser.util.Util;

@Path("/admin")
public class RestAdminProvider {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;

	@GET
	@Path("/{data_type}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User produce(@PathParam("data_type") String type, @PathParam("id") String id) {
		System.out.println(getClass().getName() + " " + request.getRequestedSessionId() + "  " + type + " id=" + id);
		User user = new User();
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();

		AppEnv env = (AppEnv) context.getAttribute("portalenv");

		/*
		 * if (id == null || id.equals("")){
		 * 
		 * }else{ user = sysDatabase.getUser(Integer.parseInt(id)); };
		 */

		return user;

	}

	@GET
	@Path("/{data_type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object produceGet(@PathParam("data_type") String type) {
		String model = ";";
		System.out.println(request.getRequestedSessionId() + " " + type);
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		UserServices us = new UserServices();
		ArrayList<SimpleUser> su = new ArrayList<SimpleUser>();

		int count = sysDatabase.getAllUsersCount("");
		int pageSize = 100;
		ArrayList<User> fl = sysDatabase.getAllUsers("", RuntimeObjUtil.calcStartEntry(1, pageSize), pageSize);
		for (User user : fl) {
			SimpleUser u = new SimpleUser();
			u.setId(user.id);
			u.setLogin(user.getLogin());
			u.setUserName(user.getUserName());
			su.add(u);

		}

		Container c = new Container(su);

		new JavaToJSON(c);
		return c;

	}


}
