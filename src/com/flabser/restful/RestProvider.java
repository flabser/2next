package com.flabser.restful;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.omg.CORBA.UserException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Element;
import com.flabser.script._Exception;
import com.flabser.script._IContent;
import com.flabser.script._IObject;
import com.flabser.script._Page;
import com.flabser.servlets.Cookies;
import com.flabser.servlets.ProviderResult;
import com.flabser.servlets.PublishAsType;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.users.ApplicationProfile;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.util.Util;



@Path("/")

public class RestProvider {
	private AppEnv env;
	@Context
	public ServletContext context;
	@Context
	public HttpServletRequest request;
	@Context
	public HttpServletResponse response;
	
	public AppEnv getAppEnv(){ 
		return (AppEnv) context.getAttribute("portalenv");
	}
	

/*	@GET
	@Path("/{model_type}")
=======
	@GET
	@Path("___conflictuet___/{model_type}")
>>>>>>> refs/remotes/origin/master
	@Produces(MediaType.APPLICATION_JSON)
	public Object produce(@PathParam("model_type") String type) throws RuleException, AuthFailedException, UserException, ClassNotFoundException, InstantiationException, IllegalAccessException, DatabasePoolException {
		System.out.println(getClass().getName() + " " + request.getRequestedSessionId() + "  " + type + " id=");
		User user = new User();
		HttpSession jses = null;
		UserSession userSession = null;
		ArrayList<_IContent> result = null;
				
		env = (AppEnv) context.getAttribute("portalenv");
		IRule rule = env.ruleProvider.getRule(type);

		if (rule != null) {
			if (!rule.isAnonymousAccessAllowed()) {
				jses = request.getSession(true);
				userSession = (UserSession) jses.getAttribute("usersession");
				if (userSession == null)
					throw new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, null);
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
			Cookies cook = new Cookies(request);
			//userSession.lang = cook.currentLang;
			
			ApplicationProfile ap = new ApplicationProfile();
			ap.appName = env.appType;
			ap.owner = "justaidajam@gmail.com";						
			ap.dbLogin = "justaidajam_gmail_com";
			ap.dbName = "cashtracker_justaidajam_gmail_com";
			ap.dbPwd = "NV07420M84NCW5V8";
			user.addApplication(ap);
			userSession = new UserSession(user,"com.flabser.solutions.cashtracker.Database", env.getName());
		}

		try {
			result = page(response, request, rule, userSession);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (_Exception e) {
			e.printStackTrace();
		}
		_Element c = (_Element) result.get(0);
		
		Object r = c.getValue();
		System.out.println(r.getClass().getName());
		Container l = new Container(r);
		System.out.println(l.getClass().getName());
		new JavaToJSON(l);
		return l;

	}
	
	
	@POST
	@Path("/{model_type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public com.flabser.restful.SignIn producePost(Object object) {
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

		 Response json = //convert entity to json
		  return Response.ok(json, MediaType.APPLICATION_JSON).build();
	//	return Response.status(200).entity(object.getClass().getName()).build();
	//	return result;
		HashMap<String, Object> f = new HashMap<String,Object>();
		f.put("login", "it is login");
		f.put("pwd", "taht is password");
		com.flabser.restful.SignIn u = new com.flabser.restful.SignIn(f);
		return u;
		//return new User("ss","ss");
	//	return Response.status(201).entity(new User("ss","ss")).build();

	}
	
	private ArrayList<_IContent> page(HttpServletResponse response, HttpServletRequest request, IRule rule,
			UserSession userSession) throws RuleException, UnsupportedEncodingException, ClassNotFoundException, _Exception {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		result.addHistory = pageRule.addToHistory;
		HashMap<String, String[]> fields = new HashMap<String, String[]>();
		Map<String, String[]> parMap = request.getParameterMap();
		fields.putAll(parMap);
		Page page = new Page(env, userSession, pageRule, request.getMethod());
		result.output.append(page.process(fields).toXML());
		if (page.fileGenerated) {
			result.publishAs = PublishAsType.OUTPUTSTREAM;
			result.filePath = page.generatedFilePath;
			result.originalAttachName = page.generatedFileOriginalName;
		}
		return page.process(fields).getElements();
	}*/
}
