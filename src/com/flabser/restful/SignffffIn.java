package com.flabser.restful;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.omg.CORBA.UserException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._IObject;
import com.flabser.script._Page;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.UserSession;

@Path("/auth")
public class SignffffIn {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;

	@POST
	@Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public SignIn producePost(Object object) {
		Object result = null;
		System.out.println(request.getRequestedSessionId() + "  " + object.getClass().getName());
		
		HashMap m = (HashMap)object;
		Iterator<Object> it = m.values().iterator();
		if (it.hasNext()){
			result = it.next();
			System.out.println(result);
		  
		}
		
		AppEnv env = (AppEnv) context.getAttribute("portalenv");
		
	//	_Page pojoPage = page.process(fields);
	//	return pojoPage;

	/*	 Response json = //convert entity to json
		  return Response.ok(json, MediaType.APPLICATION_JSON).build();*/
	//	return Response.status(200).entity(object.getClass().getName()).build();
	//	return result;
		HashMap<String, Object> f = new HashMap<String,Object>();
		f.put("login", "it is login");
		f.put("pwd", "taht is password");
		SignIn u = new SignIn(f);
		return u;
		//return new User("ss","ss");
	//	return Response.status(201).entity(new User("ss","ss")).build();

	}
	
	@GET
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public _Page produceGet(@PathParam("id") String id) {
		String model = ";";
		System.out.println(request.getRequestedSessionId() + " " + model );
		
		AppEnv env = (AppEnv) context.getAttribute("portalenv");
		
		_Page pojoPage = new _Page();
		pojoPage.setId("no_id");

	
		return pojoPage;

	}
	
}
