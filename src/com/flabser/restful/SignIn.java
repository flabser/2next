package com.flabser.restful;

import java.util.HashMap;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
import com.flabser.users.User;
import com.flabser.users.UserSession;

@Path("/")
public class SignIn {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public _Page producePage(@FormParam("login") String login, @FormParam("pwd") String pwd) {

		System.out.println(request.getRequestedSessionId() + " " + login + " " + pwd);
		
		AppEnv env = (AppEnv) context.getAttribute("portalenv");
		
	//	_Page pojoPage = page.process(fields);
	//	return pojoPage;

	
		return null;

	}
	
}
