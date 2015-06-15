package com.flabser.restful.admin;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.script._IObject;
import com.flabser.script._Page;
import com.flabser.users.User;

@Path("/get/user")
public class RestAdminProvider {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User produce(@PathParam("id") String id) {
		User user = new User();
		ISystemDatabase sysDatabase = DatabaseFactory.getSysDatabase();
		
		AppEnv env = (AppEnv) context.getAttribute("portalenv");

		if (id == null || id.equals("")){
			
		}else{
			user = sysDatabase.getUser(Integer.parseInt(id));
		};
			
		
		return user;

	}

}
