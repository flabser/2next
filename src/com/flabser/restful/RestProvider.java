package com.flabser.restful;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.flabser.appenv.AppEnv;
import com.flabser.exception.RuleException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.script._Page;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.servlets.Cookies;
import com.flabser.servlets.ProviderResult;
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
		Cookies cook = new Cookies(request);
		us.lang = cook.currentLang;
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
	public _Page producePage(@PathParam("id") String id, @Context UriInfo uriInfo) throws RuleException, AuthFailedException, UserException,
	ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.out.println("get page id=" + id);
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		AppEnv env = getAppEnv();
		IRule rule = env.ruleProvider.getRule(id);

		if (rule != null) {
			try {
				return page(env, (Map)queryParams, request, rule, getUserSession());
			} catch (final UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			} catch (final _Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	@GET
	@Path("/{model}")
	public _Page produceEmptyPage(@PathParam("model") String model) throws RuleException, AuthFailedException, UserException,
	ClassNotFoundException, InstantiationException, IllegalAccessException {
		String msg = "The request \"" + request.getRequestURI() + "\" has not processed by some application handler";
		Server.logger.errorLogEntry(msg);
		throw new WebApplicationException(msg, HttpServletResponse.SC_NOT_FOUND);
	}


	private _Page page(AppEnv env, Map <String, String[]> parMap, HttpServletRequest request, IRule rule,
			UserSession userSession) throws RuleException, UnsupportedEncodingException, ClassNotFoundException,
			_Exception {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		result.addHistory = pageRule.addToHistory;
		//HashMap <String, String[]> fields = new HashMap <String, String[]>();
		//Map <String, String[]> parMap = request.getParameterMap();
		//fields.putAll(parMap);
		return new Page(env, userSession, pageRule, request.getMethod()).process(parMap);
	}
}
