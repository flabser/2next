package com.flabser.server;

import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
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
import com.flabser.restful.ResourceLoader;
import com.flabser.valves.Logging;
import com.flabser.valves.Secure;
import com.flabser.valves.Unsecure;

public class WebServer implements IWebServer {
	public static final String httpSchema = "http";
	public static final String httpSecureSchema = "https";

	private static Tomcat tomcat;
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

		StandardServer server = (StandardServer) WebServer.tomcat.getServer();

		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);

		initSharedResources("/SharedResources");

	}

	public Context initSharedResources(String URLPath) throws LifecycleException, MalformedURLException {
		String db = new File("webapps/SharedResources").getAbsolutePath();
		Context sharedResContext = tomcat.addContext(URLPath, db);
		sharedResContext.setDisplayName("sharedresources");

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

		FilterDef filterAccessGuard = new FilterDef();
		filterAccessGuard.setFilterName("AccessGuard");
		filterAccessGuard.setFilterClass("com.flabser.valves.AccessGuard");

		FilterMap filterAccessGuardMapping = new FilterMap();
		filterAccessGuardMapping.setFilterName("AccessGuard");
		filterAccessGuardMapping.addServletName("Provider");

		context.addFilterDef(filterAccessGuard);
		context.addFilterMap(filterAccessGuardMapping);

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

		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service",
				new ServletContainer(new ResourceConfig(new ResourceLoader(docBase).getClasses())));
		w1.setLoadOnStartup(1);
		w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServletMapping("/rest/*", "Jersey REST Service");
		context.setTldValidation(false);

		return context;
	}

	@Override
	public Context addApplication(String appID, AppTemplate env) throws ServletException {
		Context context = null;

		Server.logger.normalLogEntry("add context \"" + env.appType + "/" + appID + "\" application...");
		String db = env.getDocBase();
		String URLPath = "/" + env.appType + "/" + appID;
		try {
			context = tomcat.addContext(URLPath, db);
			context.setDisplayName(URLPath.substring(1));

			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");

			initErrorPages(context);

			for (int i = 0; i < defaultWelcomeList.length; i++) {
				context.addWelcomeFile(defaultWelcomeList[i]);
			}

			Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
			context.addServletMapping("/", "default");

			context.addServletMapping("/Provider", "Provider");

			Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
			context.addServletMapping("/Uploader", "Uploader");

			Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
			context.addServletMapping("/Error", "Error");

			context.addMimeMapping("css", "text/css");
			context.addMimeMapping("js", "text/javascript");

			Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service", new ServletContainer(new ResourceConfig(new ResourceLoader(
					env.appType).getClasses())));
			w1.setLoadOnStartup(1);
			w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
			context.addServletMapping("/rest/*", "Jersey REST Service");
			context.setTldValidation(false);
		} catch (IllegalArgumentException iae) {
			Server.logger.warningLogEntry("Context \"" + URLPath + "\" has not been initialized");
			throw new ServletException("Context \"" + URLPath + "\" has not been initialized");
		}
		context.getServletContext().setAttribute(EnvConst.TEMPLATE_ATTR, env);
		return context;
	}

	@Override
	public Host addAppTemplate(String siteName, String URLPath, String docBase) throws LifecycleException, MalformedURLException {
		Context context = null;

		Server.logger.normalLogEntry("load \"" + docBase + "\" application template...");

		String db = new File("webapps/" + docBase).getAbsolutePath();
		context = tomcat.addContext(URLPath, db);
		context.setDisplayName(URLPath.substring(1));

		Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");

		initErrorPages(context);

		for (int i = 0; i < defaultInfoList.length; i++) {
			context.addWelcomeFile(defaultInfoList[i]);
		}

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		context.addServletMapping("/Provider", "Provider");
		context.addServletMapping("/info.html", "Provider");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service",
				new ServletContainer(new ResourceConfig(new ResourceLoader(docBase).getClasses())));
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

		for (int i = 0; i < defaultInfoList.length; i++) {
			context.addWelcomeFile(defaultInfoList[i]);
		}

		tomcat.getEngine().getPipeline().addValve(new Logging());
		tomcat.getEngine().getPipeline().addValve(new Unsecure());
		tomcat.getEngine().getPipeline().addValve(new Secure());

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
			portInfo = httpSecureSchema + "://" + tomcat.getHost().getName() + ":" + Integer.toString(Environment.secureHttpPort);
		} else {
			portInfo = tomcat.getHost().getName() + ":" + Integer.toString(Environment.httpPort);
		}
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
