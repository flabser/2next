package com.flabser.server;

import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.restful.ResourceLoader;
import com.flabser.valves.Logging;
import com.flabser.valves.Secure;
import com.flabser.valves.Unsecure;
import com.flabser.web.filter.AccessGuard;
import com.flabser.web.filter.CacheControlFilter;

public class WebServer implements IWebServer {

	public static final String httpSchema = "http";
	public static final String httpSecureSchema = "https";

	private static Tomcat tomcat;
	private static Engine engine;
	private static final String defaultWelcomeList[] = { "index.html" };
	private static final String defaultInfoList[] = { "info.html" };

	@Override
	public void init(String defaultHostName) throws MalformedURLException, LifecycleException {
		Server.logger.verboseLogEntry("init webserver ...");

		tomcat = new Tomcat();
		tomcat.setPort(Environment.httpPort);
		tomcat.setHostname(defaultHostName);
		tomcat.setBaseDir("webserver");
		tomcat.getHost().setAutoDeploy(false);
		engine = tomcat.getEngine();

		StandardServer server = (StandardServer) WebServer.tomcat.getServer();

		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);

		initSharedResources();
	}

	public Context initSharedResources() throws LifecycleException, MalformedURLException {
		String URLPath = "/" + EnvConst.SHARED_RESOURCES_NAME;
		String db = new File("webapps/" + EnvConst.SHARED_RESOURCES_NAME).getAbsolutePath();
		Context sharedResContext = tomcat.addContext(URLPath, db);
		sharedResContext.setDisplayName(EnvConst.SHARED_RESOURCES_NAME);

		addFilterToContext(sharedResContext, CacheControlFilter.class, "CacheControlFilter", "/*");

		Tomcat.addServlet(sharedResContext, "default", "org.apache.catalina.servlets.DefaultServlet");
		sharedResContext.addServletMapping("/", "default");

		sharedResContext.addMimeMapping("css", "text/css");
		sharedResContext.addMimeMapping("js", "text/javascript");
		sharedResContext.setTldValidation(false);
		return sharedResContext;
	}

	@Override
	public Context initAdministartor() {
		Context context = null;

		String docBase = EnvConst.ADMIN_APP_NAME;
		String URLPath = "/" + docBase;

		String db = new File(Environment.primaryAppDir + "webapps/" + docBase).getAbsolutePath();
		context = tomcat.addContext(URLPath, db);

		Tomcat.addServlet(context, "Provider", "com.flabser.servlets.admin.AdminProvider");
		context.setDisplayName(EnvConst.ADMIN_APP_NAME);

		addFilterToContext(context, AccessGuard.class, "AccessGuard", "/*");

		initErrorPages(context);

		for (int i = 0; i < defaultWelcomeList.length; i++) {
			context.addWelcomeFile(defaultWelcomeList[i]);
		}

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		context.addServletMapping("/Provider", "Provider");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
		context.addServletMapping("/Uploader", "Uploader");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		ResourceConfig rc = new ResourceConfig(new ResourceLoader(docBase).getClasses());
		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service", new ServletContainer(rc));
		w1.setLoadOnStartup(1);
		w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServletMapping("/rest/*", "Jersey REST Service");
		context.setTldValidation(false);

		return context;
	}

	@Override
	public Context addApplication(String appID, Site site) throws ServletException {
		Context context = null;

		Server.logger.normalLogEntry("add context \"" + site.getAppBase() + "/" + appID + "\" application...");
		String db = site.getFullPathAppBase();
		String URLPath = "/" + site.getAppBase() + "/" + appID;
		try {
			String parent = site.getParent();
			if (!parent.equals("")) {

				Site parentApp = Environment.availableTemplates.get(parent);
				Host appHost = parentApp.getHost();
				Container c = appHost.findChild(appID);
				if (c == null) {
					context = new StandardContext();
					context = tomcat.addContext(appHost, URLPath, db);
					context.setDisplayName(appID);
					context.setName(appID);
					context.setConfigured(true);
					appHost.addChild(context);
				}else{
					return null;
				}

				// engine.addChild(appHost);
			} else {
				if (tomcat.getHost().findChild(appID) == null) {
					context = tomcat.addContext(URLPath, db);
					context.setDisplayName(URLPath);
					context.setName(appID);
				}else{
					return null;
				}
			}

			initErrorPages(context);
			addFilterToContext(context, CacheControlFilter.class, "CacheControlFilter", "/*");

			for (int i = 0; i < defaultWelcomeList.length; i++) {
				context.addWelcomeFile(defaultWelcomeList[i]);
			}

			Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
			context.addServletMapping("/", "default");

			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");
			context.addServletMapping("/Provider", "Provider");

			Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
			context.addServletMapping("/Uploader", "Uploader");

			Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
			context.addServletMapping("/Error", "Error");

			context.addMimeMapping("css", "text/css");
			context.addMimeMapping("js", "text/javascript");

			ResourceConfig rc = new ResourceConfig(new ResourceLoader(site.getAppBase()).getClasses());
			Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service", new ServletContainer(rc));
			w1.setLoadOnStartup(1);
			w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
			context.addServletMapping("/rest/*", "Jersey REST Service");
			context.setTldValidation(false);
			context.getServletContext().setAttribute(EnvConst.TEMPLATE_ATTR, site.getAppTemlate());
		} catch (IllegalArgumentException iae) {
			if (!iae.getMessage().contains("is not unique")){
				Server.logger.warningLogEntry("Context \"" + URLPath + "\" has not been initialized");
				throw new ServletException("Context \"" + URLPath + "\" has not been initialized");
			}else{
				Server.logger.warningLogEntry("Context \"" + URLPath + "\" has been already initialized");
			}
		}


		return context;
	}

	@Override
	public Host addAppTemplate(Site site) throws LifecycleException, MalformedURLException {
		Context context = null;

		String templateName = site.getAppBase();
		String h = "";
		if (!site.getVirtualHostName().equals("")) {
			h = "(" + site.getVirtualHostName() + ")";
		}
		Server.logger.normalLogEntry("load \"" + templateName + h + "\"");
		String docBase = site.getFullPathAppBase();

		if (site.getVirtualHostName().equals("")) {
			String parent = site.getParent();
			if (!parent.equals("")) {
				Site parentSite = Environment.availableTemplates.get(parent);
				Host appHost = parentSite.getHost();
				context = new StandardContext();
				context = tomcat.addContext(appHost, "/" + templateName, docBase);
				context.setDisplayName(templateName);
				context.setName(templateName);
				context.setConfigured(true);
				appHost.addChild(context);
				site.setVirtualHostName(parentSite.getVirtualHostName());
			} else {
				context = tomcat.addContext("/" + templateName, docBase);
				context.setDisplayName(templateName);
			}

		} else {
			Host appHost = site.getHost();
			context = new StandardContext();
			context = tomcat.addContext(appHost, "", docBase);
			context.setDisplayName(templateName);
			context.setName(templateName);
			for (int i = 0; i < defaultWelcomeList.length; i++) {
				context.addWelcomeFile(defaultWelcomeList[i]);
			}
			appHost.addChild(context);

			String srDocBase = EnvConst.SHARED_RESOURCES_NAME;
			Context shContext = new StandardContext();
			String sharedResDb = new File("webapps/" + EnvConst.SHARED_RESOURCES_NAME).getAbsolutePath();
			shContext = tomcat.addContext(appHost, "/" + EnvConst.SHARED_RESOURCES_NAME, sharedResDb);
			shContext.setDisplayName(srDocBase);
			shContext.setName(srDocBase);
			Tomcat.addServlet(shContext, "default", "org.apache.catalina.servlets.DefaultServlet");
			shContext.addServletMapping("/", "default");

			shContext.addMimeMapping("css", "text/css");
			shContext.addMimeMapping("js", "text/javascript");
			shContext.setConfigured(true);

			engine.addChild(appHost);
		}

		initErrorPages(context);

		/*		for (int i = 0; i < defaultInfoList.length; i++) {
			context.addWelcomeFile(defaultInfoList[i]);
		}*/

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");
		context.addServletMapping("/Provider", "Provider");
		context.addServletMapping("/index.html", "Provider");
		//context.addServletMapping("/info.html", "Provider");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		ResourceConfig rc = new ResourceConfig(new ResourceLoader(templateName).getClasses());
		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service", new ServletContainer(rc));
		w1.setLoadOnStartup(1);
		w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServletMapping("/rest/*", "Jersey REST Service");
		context.setTldValidation(false);

		return null;
	}

	@Override
	public void initDefaultURL() {
		String db = new File(Environment.primaryAppDir + "webapps/ROOT").getAbsolutePath();
		Context context = tomcat.addContext(tomcat.getHost(), "", db);
		context.setDisplayName("root");

		/*	for (int i = 0; i < defaultInfoList.length; i++) {
			context.addWelcomeFile(defaultInfoList[i]);
		}*/

		engine.getPipeline().addValve(new Logging());
		engine.getPipeline().addValve(new Unsecure());
		engine.getPipeline().addValve(new Secure());

		initErrorPages(context);

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");
	}

	@Override
	public String initConnectors() {
		String portInfo = "";

		if (Environment.isTLSEnable) {
			Connector secureConnector = null;
			Server.logger.normalLogEntry("TLS has been enabled");
			secureConnector = tomcat.getConnector();
			secureConnector.setPort(Environment.secureHttpPort);
			secureConnector.setScheme(httpSecureSchema);
			secureConnector.setProtocol("org.apache.coyote.http11.Http11AprProtocol");
			secureConnector.setSecure(true);
			secureConnector.setEnableLookups(false);
			secureConnector.setSecure(true);
			secureConnector.setProperty("SSLEnabled", "true");
			secureConnector.setProperty("SSLCertificateFile", Environment.certFile);
			secureConnector.setProperty("SSLCertificateKeyFile", Environment.certKeyFile);
			tomcat.setConnector(secureConnector);

			portInfo = httpSecureSchema + "://" + tomcat.getHost().getName() + ":"
					+ Integer.toString(Environment.secureHttpPort);
		} else {
			portInfo = tomcat.getHost().getName() + ":" + Integer.toString(Environment.httpPort);
		}

		Connector connector = tomcat.getConnector();
		connector.setProperty("compression", "on");
		connector.setProperty("compressionMinSize", "1024");
		connector.setProperty("noCompressionUserAgents", "gozilla, traviata");
		connector.setProperty("compressableMimeType",
				"text/html, text/xml, text/plain, text/css, text/javascript, application/json,"
						+ "application/javascript, application/x-javascript");

		return portInfo;
	}

	@Override
	public void startContainer() {
		try {
			tomcat.start();
		} catch (LifecycleException e) {
			Server.logger.errorLogEntry(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				stopContainer();
			}
		});
	}

	@Override
	public synchronized void stopContainer() {
		try {
			if (tomcat != null) {
				tomcat.stop();
			}
		} catch (LifecycleException exception) {
			Server.logger.errorLogEntry("cannot stop WebServer normally " + exception.getMessage());
		}
	}

	private void addFilterToContext(Context context, Class<?> filterClass, String filterName, String... urlPattern) {
		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName(filterName);
		filterDef.setFilterClass(filterClass.getName());

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName(filterName);
		for (String path : urlPattern) {
			filterMap.addURLPattern(path);
		}

		context.addFilterDef(filterDef);
		context.addFilterMap(filterMap);
	}

	private Context initContex(String siteName, AppTemplate env, String appID) {
		String db = new File("webapps/" + env.templateType).getAbsolutePath();

		Host appHost = new StandardHost();
		appHost.setName(appID + "." + siteName);
		// appHost.setAppBase(db);
		Context context = new StandardContext();
		context = tomcat.addContext(appHost, "/" + appID, db);

		context.setDocBase(db);
		context.setDisplayName(appID);
		context.setName(appID);
		context.setPath("");
		return context;
	}

	private void initErrorPages(Context context) {
		ErrorPage er = new ErrorPage();
		er.setErrorCode(HttpServletResponse.SC_NOT_FOUND);
		er.setLocation("/Error");
		context.addErrorPage(er);
		ErrorPage er401 = new ErrorPage();
		er401.setErrorCode(HttpServletResponse.SC_UNAUTHORIZED);
		er401.setLocation("/Error");
		context.addErrorPage(er401);
		ErrorPage er400 = new ErrorPage();
		er400.setErrorCode(HttpServletResponse.SC_BAD_REQUEST);
		er400.setLocation("/Error");
		context.addErrorPage(er);
		ErrorPage er500 = new ErrorPage();
		er500.setErrorCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		er500.setLocation("/Error");
		context.addErrorPage(er);
	}
}
