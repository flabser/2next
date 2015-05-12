package com.flabser.server;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import com.flabser.dataengine.IDatabase;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.log.Log4jLogger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;

public class Server{
	public static com.flabser.log.ILogger logger;
	public static final String serverVersion = "1.0.0";
	public static String compilationTime = "";
	public static final String serverTitle = "2Next" + serverVersion;
	public static Date startTime = new Date();
	public static IDatabase dataBase;
	public static IWebServer webServerInst;
	
	public static void start() throws MalformedURLException, LifecycleException, URISyntaxException{		
		logger = new Log4jLogger("");
		logger.normalLogEntry(serverTitle + " start");
		compilationTime = ((Log4jLogger) logger).getBuildDateTime();
		
		logger.normalLogEntry("Copyright(c) the F developers team 2015. All Right Reserved");
		logger.normalLogEntry("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "(" + System.getProperty("os.arch") + "), jvm: " + System.getProperty("java.version"));	

		Environment.init();
		logger.verboseLogEntry("Build " + compilationTime);
		webServerInst = WebServerFactory.getServer(Environment.serverVersion);
		webServerInst.init(Environment.hostName);
		
		if(Environment.adminConsoleEnable){				
			Host host = webServerInst.addApplication("Administrator", "/Administrator", "Administrator");
			
			HashSet<Host> hosts = new HashSet<Host>();
			hosts.add(host);			
		}
		
		/*if(Environment.adminConsoleEnable){				
			Host host = webServerInst.addJaxRestHandler() ;
		
			HashSet<Host> hosts = new HashSet<Host>();
			hosts.add(host);			
		}*/
		
		Server.logger.normalLogEntry("Applications are starting...");
				
		HashSet<Host> hosts = new HashSet<Host>();
		for(Site webApp: Environment.webAppToStart.values()){			
			hosts.add(webServerInst.addApplication(webApp.name, "/" + webApp.appBase, webApp.appBase));
			Server.logger.verboseLogEntry(webApp.name + " " + webApp.appBase);
			
		}

		String info = webServerInst.initConnectors();
		Server.logger.verboseLogEntry("Webserver start ("  + info + ")");
		webServerInst.startContainer();
		  		
	}

	
	public static void main(String[] arg){
		try {
			Server.start();
		} catch (MalformedURLException e) {	
			e.printStackTrace();
		} catch (LifecycleException e) {		
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void shutdown(){
		logger.normalLogEntry("Server is stopping ... ");
		
		Environment.shutdown();	
		//webServerInst.stopContainer();
		System.exit(0);
	}
}
