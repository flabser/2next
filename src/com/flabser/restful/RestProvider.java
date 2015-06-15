package com.flabser.restful;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
public class RestProvider {

	@Context
	ServletContext context;
	@Context
	HttpServletRequest request;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public _Page producePage(@PathParam("id") String id) {

		HttpSession jses = null;
		UserSession userSession = null;

		AppEnv env = (AppEnv) context.getAttribute("portalenv");
		IRule rule;
		try {
			rule = env.ruleProvider.getRule(id);
			if (rule != null) {
				if (!rule.isAnonymousAccessAllowed()) {
					jses = request.getSession(true);
					userSession = (UserSession) jses.getAttribute("usersession");
					if (userSession == null) throw new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION,
							null);
				} else {
					jses = request.getSession(false);
					if (jses == null) {
						jses = request.getSession(true);
						userSession = new UserSession(new User());
						jses.setAttribute("usersession", userSession);
					} else {
						userSession = (UserSession) jses.getAttribute("usersession");
						if (userSession == null) {
							userSession = new UserSession(new User());
							jses.setAttribute("usersession", userSession);
						}
					}
				}

			}
			PageRule pageRule = (PageRule) rule;
			HashMap <String, String[]> fields = new HashMap <String, String[]>();
			Map <String, String[]> parMap = request.getParameterMap();
			fields.putAll(parMap);
			Page page = new Page(env, userSession, pageRule);
			_Page pojoPage = page.process(fields);
			return pojoPage;

		} catch (RuleException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		} catch (AuthFailedException e) {
			e.printStackTrace();
		}
		return null;

	}

	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public _Page save(_IObject c) {

		System.out.println(c);
		return null;
	}

}
