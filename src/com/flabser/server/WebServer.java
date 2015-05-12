package com.flabser.server;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import com.flabser.env.Environment;
import java.io.File;
import java.net.MalformedURLException;


public class WebServer implements IWebServer {
	private Tomcat tomcat;
	private static final String defaultWelcomeList[]={"index.html", "index.htm"};

	@Override
	public void init(String defaultHostName) throws MalformedURLException, LifecycleException {
		Server.logger.verboseLogEntry("Init webserver ...");

		tomcat = new Tomcat();                                    
		tomcat.setPort(Environment.httpPort);   
		tomcat.setHostname(defaultHostName);
		tomcat.setBaseDir("webserver"); 

		StandardServer server = (StandardServer) this.tomcat.getServer();
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);  

		getSharedResources("/SharedResources");
		initDefaultURL();
		
	}

	public Context getSharedResources(String URLPath) throws LifecycleException, MalformedURLException {	
		String db = new File("webapps/SharedResources").getAbsolutePath();
		Context sharedResContext = tomcat.addContext(URLPath, db);
		sharedResContext.setDisplayName("sharedresources");

		Tomcat.addServlet(sharedResContext, "default", "org.apache.catalina.servlets.DefaultServlet");
		sharedResContext.addServletMapping("/", "default");	

		sharedResContext.addMimeMapping("css", "text/css");
		sharedResContext.addMimeMapping("js", "text/javascript");

		return sharedResContext;			
	}

	public Host addApplication(String siteName, String URLPath, String docBase) throws LifecycleException, MalformedURLException {		
		Context context = null;

		if (docBase.equalsIgnoreCase("Administrator")){
			String db = new File(Environment.primaryAppDir + "webapps/" + docBase).getAbsolutePath();
			context = tomcat.addContext(URLPath, db);		
			for( int i=0; i< defaultWelcomeList.length; i++ ){
				context.addWelcomeFile( defaultWelcomeList[i]);			
			}
			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.admin.AdminProvider");
			context.setDisplayName("Administrator");
		}else{
			if (siteName == null || siteName.equalsIgnoreCase("")){				
				String db = new File("webapps/" + docBase).getAbsolutePath();
				context = tomcat.addContext(URLPath, db);	
				context.setDisplayName(URLPath.substring(1));
			}else{
				URLPath = "";
				StandardHost appHost = new StandardHost();			
				appHost.setName(siteName);
				String baseDir = new File("webapps/" + docBase).getAbsolutePath();
				appHost.setAppBase(baseDir);

				context = tomcat.addContext(appHost, URLPath, baseDir);
				context.setDisplayName(siteName);
			}	
			context.addWelcomeFile("Provider");	
			Tomcat.addServlet(context, "Provider", "com.flabser.servlets.Provider");		
		}

		Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
		context.addServletMapping("/", "default");



		context.addServletMapping("/Provider", "Provider");

		FilterDef filterAccessGuard = new FilterDef();
		filterAccessGuard.setFilterName("AccessGuard");
		filterAccessGuard.setFilterClass("com.flabser.filters.AccessGuard");

		FilterMap filterAccessGuardMapping = new FilterMap();
		filterAccessGuardMapping.setFilterName("AccessGuard");
		filterAccessGuardMapping.addServletName("Provider");

		context.addFilterDef(filterAccessGuard);
		context.addFilterMap(filterAccessGuardMapping);		

		Tomcat.addServlet(context, "Login", "com.flabser.servlets.Login");
		context.addServletMapping("/Login", "Login");

		Tomcat.addServlet(context, "Logout", "com.flabser.servlets.Logout");
		context.addServletMapping("/Logout", "Logout");

		Wrapper w = Tomcat.addServlet(context, "PortalInit", "com.flabser.servlets.PortalInit");	
		w.setLoadOnStartup(1);

		context.addServletMapping("/PortalInit", "PortalInit");

		Tomcat.addServlet(context, "Uploader", "com.flabser.servlets.Uploader");
		context.addServletMapping("/Uploader", "Uploader");

		Tomcat.addServlet(context, "Error", "com.flabser.servlets.Error");
		context.addServletMapping("/Error", "Error");

		context.addMimeMapping("css", "text/css");
		context.addMimeMapping("js", "text/javascript");



		return null;
	}

	public Context initDefaultURL() throws LifecycleException, MalformedURLException {
		String db = new File(Environment.primaryAppDir + "webapps/Administrator").getAbsolutePath();
		//String db = new File("webapps/Administrator").getAbsolutePath();
		Context context = tomcat.addContext("", db);

		//	Tomcat.addServlet(context, "Redirector", "kz.flabs.servlets.Redirector");
		//	context.addServletMapping("/", "Redirector");

		return context;			
	}

	
	public String initConnectors(){
		String portInfo = "";
		if (Environment.isSSLEnable){
			Connector secureConnector = null;	
			Server.logger.normalLogEntry("TLS connector has been enabled");
			secureConnector = tomcat.getConnector();
			//		secureConnector.setDomain("flabs.kz");
			secureConnector.setPort(Environment.secureHttpPort);	
			secureConnector.setScheme("https");
			secureConnector.setProtocol("org.apache.coyote.http11.Http11Protocol");
			secureConnector.setSecure(true);
			secureConnector.setEnableLookups(false);		
			secureConnector.setSecure(true);
			secureConnector.setProperty("SSLEnabled","true");
			secureConnector.setProperty("sslProtocol", "TLS");		
			secureConnector.setProperty("keystoreFile", Environment.keyStore);
			secureConnector.setProperty("keystorePass", Environment.keyPwd);
			if (Environment.isClientSSLAuthEnable){
				secureConnector.setProperty("clientAuth", "true");
				secureConnector.setProperty("truststoreFile", Environment.trustStore);
				secureConnector.setProperty("truststorePass", Environment.trustStorePwd);
			}	
			tomcat.setConnector(secureConnector);
			portInfo = "secure:" + tomcat.getHost().getName() + ":" + Integer.toString(Environment.secureHttpPort);
		}else{
			portInfo = tomcat.getHost().getName() + ":" + Integer.toString(Environment.httpPort);
		}	
		return portInfo;	

	}

	public void startContainer(){	
		try {
			tomcat.start();
			tomcat.getServer().await();
		} catch (LifecycleException e) {	
			Server.logger.errorLogEntry(e);		
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				stopContainer();
			}
		});
	}

	public synchronized void stopContainer() {
		try {
			if (tomcat != null) {
				tomcat.stop();
			}
		} catch (LifecycleException exception) {
			Server.logger.errorLogEntry("Cannot Stop WebServer" + exception.getMessage());			
		}

	}

	@Override
	public Host addJaxRestHandler() throws LifecycleException,	MalformedURLException {
		Context context = null;


		String db = new File(Environment.primaryAppDir + "webapps/JaxREST").getAbsolutePath();
		context = tomcat.addContext("/JaxREST", db);		

		context.setDisplayName("JaxREST");


		//	Wrapper w = Tomcat.addServlet(context, "REST", "com.sun.jersey.spi.container.servlet.ServletContainer");	
		//	w.setLoadOnStartup(1);
		//	context.addParameter("com.sun.jersey.config.property.packages", "com.javacodegeeks.enterprise.rest.jersey");

		//	context.addServletMapping("/rest", "REST");


		return null;
	}



}
