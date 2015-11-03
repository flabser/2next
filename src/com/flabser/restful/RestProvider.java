package com.flabser.restful;

import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.exception.AuthFailedException;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.scheduler.tasks.TempFileCleaner;
import com.flabser.script._Page;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.users.User;
import com.flabser.users.UserSession;

@Path("/")
public class RestProvider {

	@Context
	protected ServletContext context;
	@Context
	public HttpServletRequest request;
	@Context
	protected HttpServletResponse response;

	public AppTemplate getAppTemplate() {
		return (AppTemplate) context.getAttribute(EnvConst.TEMPLATE_ATTR);

	}

	public UserSession getUserSession() {
		HttpSession jses = request.getSession(false);
		UserSession us = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);
		return us;

	}

	public String getAppID() {
		return (String) request.getAttribute("appid");

	}

	public _Session getSession() {
		UserSession userSession = getUserSession();
		if (userSession == null) {
			return null;
		} else {
			return new _Session(getAppTemplate(), userSession, getAppID());
		}
	}

	@GET
	@Path("/page/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response producePage(@PathParam("id") String id, @Context UriInfo uriInfo) throws RuleException,
			AuthFailedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		AppTemplate env = getAppTemplate();
		IRule rule = env.ruleProvider.getRule(id);
		_Page result = null;

		if (rule != null) {
			try {
				try {

					result = page(env, queryParams, request, rule, getUserSession());
				} catch (WebFormValueException e) {
					return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(result).build();
				}
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return Response.status(HttpServletResponse.SC_OK).entity(result).build();
	}

	@POST
	@Path("/page/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response proPage(@PathParam("id") String id, MultivaluedMap<String, String> formParams) throws RuleException,
			AuthFailedException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		AppTemplate env = getAppTemplate();
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
	@Path("/stream/{model}/{id}/{field}/{file}")
	public Response produceStream(@PathParam("model") String model, @PathParam("id") long id,
			@PathParam("field") String fieldName, @PathParam("file") String fileName) {
		File file = null;
		String fn = null;
		if (!model.equalsIgnoreCase("users")) {

		} else {
			ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
			User user = sysDb.getUser(id);

			if (fieldName.equalsIgnoreCase("avatar")) {
				File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
				fn = userTmpDir.getAbsolutePath() + File.separator + "___" + user.getAvatar().getRealFileName();
				File fileToWriteTo = new File(fn);
				byte[] fileAsByteArray = sysDb.getUserAvatarStream(user.id);
				try {
					FileUtils.writeByteArrayToFile(fileToWriteTo, fileAsByteArray);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}

				file = new File(fn);
			}
		}
		if (file != null && file.exists()) {
			TempFileCleaner.addFileToDelete(fn);
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
		} else {
			return Response.status(HttpServletResponse.SC_BAD_REQUEST).build();
		}
	}

	@GET
	@Path("/{model}")
	public _Page produceEmptyPage(@PathParam("model") String model) throws RuleException, AuthFailedException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {
		String msg = "The request \"" + request.getRequestURI() + "\" has not processed by some application handler";
		Server.logger.errorLogEntry(msg);
		throw new WebApplicationException(msg, HttpServletResponse.SC_NOT_FOUND);
	}

	/*
	 * private _Page page(AppTemplate env, Map<String, String[]> parMap,
	 * HttpServletRequest request, IRule rule, UserSession userSession) throws
	 * RuleException, UnsupportedEncodingException, ClassNotFoundException,
	 * _Exception, WebFormValueException { PageRule pageRule = (PageRule) rule;
	 * return new Page(env, userSession, pageRule, request.getMethod(),
	 * context.getServletContextName()).process(parMap); }
	 */

	private _Page page(AppTemplate env, MultivaluedMap<String, String> formParams, HttpServletRequest request2,
			IRule rule, UserSession userSession) throws ClassNotFoundException, RuleException, WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		Map<String, String[]> parMap = new HashMap<String, String[]>();
		for (String e : formParams.keySet()) {
			String v[] = new String[1];
			v[0] = formParams.getFirst(e);
			parMap.put(e, v);
		}
		return new Page(env, userSession, pageRule, request.getMethod(), context.getServletContextName())
				.process(parMap);
	}
}
