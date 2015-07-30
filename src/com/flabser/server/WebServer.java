package com.flabser.server;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.filters.AccessValve;
import com.flabser.restful.ResourceLoader;

public class WebServer implements IWebServer {
	public static final String httpSchema = "http";
	public static final String httpSecureSchema = "https";

	private static Tomcat tomcat;
	private static final String defaultWelcomeList[] = { "index.html", "index.htm" };

	@Override
	public void init(String defaultHostName) throws MalformedURLException, LifecycleException {
		Server.logger.verboseLogEntry("init webserver ...");

		tomcat = new Tomcat();
		tomcat.setPort(Environment.httpPort);
		tomcat.setHostname(defaultHostName);
		tomcat.setBaseDir("webserver");

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
	public Context addApplication(String appID, String appType) throws LifecycleException, MalformedURLException {
		Context context = null;
		AppEnv env = Environment.getApplication(appType);

		Server.logger.normalLogEntry("load \"" + env.appType + "/" + appID + "\" application...");

		String db = env.getDocBase();
		String URLPath = "/" + env.appType + "/" + appID;
		context = tomcat.addContext(URLPath, db);
		context.setDisplayName(URLPath.substring(1));

		Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");

		for (int i = 0; i < defaultWelcomeList.length; i++) {
			context.addWelcomeFile(defaultWelcomeList[i]);
		}

		FilterDef filterAccessGuard = new FilterDef();
		filterAccessGuard.setFilterName("AccessGuard");
		filterAccessGuard.setFilterClass("com.flabser.filters.AccessGuard");

		FilterMap filterAccessGuardMapping = new FilterMap();
		filterAccessGuardMapping.setFilterName("AccessGuard");
		// filterAccessGuardMapping.addServletName("Provider");
		filterAccessGuardMapping.addURLPattern("/*");

		context.addFilterDef(filterAccessGuard);
		context.addFilterMap(filterAccessGuardMapping);

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		context.addServletMapping("/Provider", "Provider");

		Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
		context.addServletMapping("/Uploader", "Uploader");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service",
				new ServletContainer(new ResourceConfig(new ResourceLoader(env.getDocBase()).getClasses())));
		w1.setLoadOnStartup(1);
		w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServletMapping("/rest/*", "Jersey REST Service");
		filterAccessGuardMapping.addServletName("Jersey REST Service");
		context.setTldValidation(false);

		return context;
	}

	@Override
	public Context initAppEnv(String appType) {
		Context context = null;

		Server.logger.normalLogEntry("init \"" + appType + "\" environment...");

		String db = new File("webapps/" + appType).getAbsolutePath();
		String URLPath = "/" + appType;
		context = tomcat.addContext(URLPath, db);
		context.setDisplayName(appType);

		for (int i = 0; i < defaultWelcomeList.length; i++) {
			context.addWelcomeFile(defaultWelcomeList[i]);
		}

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		FilterDef filterAccessGuard = new FilterDef();
		filterAccessGuard.setFilterName("AccessGuard");
		filterAccessGuard.setFilterClass("com.flabser.filters.AccessGuard");

		FilterMap filterAccessGuardMapping = new FilterMap();
		filterAccessGuardMapping.setFilterName("AccessGuard");
		filterAccessGuardMapping.addURLPattern("/*");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		return null;
	}

	@Override
	public Host addApplication(String siteName, String URLPath, String docBase) throws LifecycleException, MalformedURLException {
		Context context = null;

		if (docBase.equalsIgnoreCase("Administrator")) {
			String db = new File(Environment.primaryAppDir + "webapps/" + docBase).getAbsolutePath();
			context = tomcat.addContext(URLPath, db);

			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.admin.AdminProvider");
			context.setDisplayName("Administrator");
		} else {
			Server.logger.normalLogEntry("load \"" + docBase + "\" application...");

			String db = new File("webapps/" + docBase).getAbsolutePath();
			context = tomcat.addContext(URLPath, db);
			context.setDisplayName(URLPath.substring(1));

			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");
		}

		for (int i = 0; i < defaultWelcomeList.length; i++) {
			context.addWelcomeFile(defaultWelcomeList[i]);
		}

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		context.addServletMapping("/Provider", "Provider");

		// FilterDef filterAccessGuard = new FilterDef();
		// filterAccessGuard.setFilterName("AccessGuard");
		// filterAccessGuard.setFilterClass("com.flabser.filters.AccessGuard");

		// FilterMap filterAccessGuardMapping = new FilterMap();
		// filterAccessGuardMapping.setFilterName("AccessGuard");
		// filterAccessGuardMapping.addServletName("Provider");
		// filterAccessGuardMapping.addURLPattern("/*");

		// context.addFilterDef(filterAccessGuard);
		// context.addFilterMap(filterAccessGuardMapping);

		// Tomcat.addServlet(context, "Login", "com.flabser.servlets.Login");
		// context.addServletMapping("/Login", "Login");

		// Tomcat.addServlet(context, "Logout", "com.flabser.servlets.Logout");
		// context.addServletMapping("/Logout", "Logout");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
		context.addServletMapping("/Uploader", "Uploader");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");

		Wrapper w1 = Tomcat.addServlet(context, "Jersey REST Service", new ServletContainer(new ResourceConfig(new ResourceLoader(docBase).getClasses())));
		w1.setLoadOnStartup(1);
		w1.addInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServletMapping("/rest/*", "Jersey REST Service");
		// filterAccessGuardMapping.addServletName("Jersey REST Service");

		context.setTldValidation(false);

		return null;
	}

	@Override
	public void initDefaultURL(Host host) {
		String db = new File(Environment.primaryAppDir + "webapps/Nubis").getAbsolutePath();
		Context context = tomcat.addContext(host, "", db);
		context.setDisplayName("root");

		for (int i = 0; i < defaultWelcomeList.length; i++) {
			context.addWelcomeFile(defaultWelcomeList[i]);
		}

		tomcat.getEngine().getPipeline().addValve(new AccessValve());

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");

		/*
		 * Tomcat.addServlet(context, "Provider",
		 * "com.flabser.servlets.Provider");
		 * context.addServletMapping("/Provider", "Provider"); AppEnv env =
		 * Environment.getApplication("Nubis");
		 * context.getServletContext().setAttribute(AppEnv.APP_ATTR, env);
		 */

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
			// tomcat.getServer().await();
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

}
