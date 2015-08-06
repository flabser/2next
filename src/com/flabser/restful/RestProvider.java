package com.flabser.restful;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.flabser.appenv.AppEnv;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.script._Page;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.users.AuthFailedException;
import com.flabser.users.UserException;
import com.flabser.users.UserSession;

@Path("/")
public class RestProvider {

	@Context
	private ServletContext context;
	@Context
	public HttpServletRequest request;
	@Context
	protected HttpServletResponse response;

	public AppEnv getAppEnv() {
		return (AppEnv) context.getAttribute(AppEnv.APP_ATTR);

	}

	public UserSession getUserSession() {
		HttpSession jses = request.getSession(true);
		UserSession us = (UserSession) jses.getAttribute(UserSession.SESSION_ATTR);
		if (us == null) {
			us = new UserSession(new com.flabser.users.User());
		}
		return us;

	}

	public _Session getSession() {
		UserSession userSession = getUserSession();
		if (userSession == null) {
			return null;
		} else {
			return new _Session(getAppEnv(), userSession);
		}
	}

	@GET
	@Path("/page/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response producePage(@PathParam("id") String id, @Context UriInfo uriInfo) throws RuleException, AuthFailedException, UserException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("get page id=" + id);
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		AppEnv env = getAppEnv();
		IRule rule = env.ruleProvider.getRule(id);
		_Page result = null;

		if (rule != null) {
			try {
				try {

					result = page(env, (Map) queryParams, request, rule, getUserSession());
				} catch (WebFormValueException e) {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(result).build();
				}
			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			} catch (final _Exception e) {
				e.printStackTrace();
			}

		}
		return Response.status(HttpServletResponse.SC_OK).entity(result).build();
	}

	@POST
	@Path("/page/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response proPage(@PathParam("id") String id, MultivaluedMap<String, String> formParams) throws RuleException, AuthFailedException, UserException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("get page id=" + id);
		AppEnv env = getAppEnv();
		IRule rule = env.ruleProvider.getRule(id);
		_Page result = null;

		if (rule != null) {
			try {
				result = page(env, formParams, request, rule, getUserSession());
			} catch (WebFormValueException e) {
				return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(result).build();
			}

		}
		return Response.status(HttpServletResponse.SC_OK).entity(result).build();
	}

	@GET
	@Path("/{model}")
	public _Page produceEmptyPage(@PathParam("model") String model) throws RuleException, AuthFailedException, UserException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		String msg = "The request \"" + request.getRequestURI() + "\" has not processed by some application handler";
		Server.logger.errorLogEntry(msg);
		throw new WebApplicationException(msg, HttpServletResponse.SC_NOT_FOUND);
	}

	private _Page page(AppEnv env, Map<String, String[]> parMap, HttpServletRequest request, IRule rule, UserSession userSession) throws RuleException,
			UnsupportedEncodingException, ClassNotFoundException, _Exception, WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		return new Page(env, userSession, pageRule, request.getMethod()).process(parMap);
	}

	private _Page page(AppEnv env, MultivaluedMap<String, String> formParams, HttpServletRequest request2, IRule rule, UserSession userSession)
			throws ClassNotFoundException, RuleException, WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		Map<String, String[]> parMap = new HashMap<String, String[]>();
		for (String e : formParams.keySet()) {
			String v[] = new String[1];
			v[0] = formParams.getFirst(e);
			parMap.put(e, v);
		}
		return new Page(env, userSession, pageRule, request.getMethod()).process(parMap);
	}
}
