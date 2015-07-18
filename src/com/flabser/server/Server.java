package com.flabser.server;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;

import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;

import com.flabser.dataengine.IDatabase;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.log.Log4jLogger;
import com.flabser.scheduler.PeriodicalServices;

public class Server {
	public static com.flabser.log.ILogger logger;
	public static final String serverVersion = "1.0.0";
	public static String compilationTime = "";
	public static final String serverTitle = "2Next " + serverVersion;
	public static Date startTime = new Date();
	public static IDatabase dataBase;
	public static IWebServer webServerInst;

	public static void start() throws MalformedURLException,
			LifecycleException, URISyntaxException {
		logger = new Log4jLogger("");
		logger.normalLogEntry(serverTitle + " start");
		compilationTime = ((Log4jLogger) logger).getBuildDateTime();

		logger.normalLogEntry("Copyright(c) the F developers team 2015. All Right Reserved");
		logger.normalLogEntry("OS: " + System.getProperty("os.name") + " "
				+ System.getProperty("os.version") + "("
				+ System.getProperty("os.arch") + "), jvm: "
				+ System.getProperty("java.version"));

		Environment.init();
		if (!compilationTime.equalsIgnoreCase(""))
			logger.verboseLogEntry("Build: " + compilationTime);
		webServerInst = WebServerFactory.getServer(Environment.serverVersion);
		webServerInst.init(Environment.hostName);

		Host host = webServerInst.addApplication("Administrator",
				"/Administrator", "Administrator");

		HashSet<Host> hosts = new HashSet<Host>();
		hosts.add(host);

		for (Site webApp : Environment.webAppToStart.values()) {
			hosts.add(webServerInst.addApplication(webApp.name, "/"
					+ webApp.appBase, webApp.appBase));
		}

		String info = webServerInst.initConnectors();
		Server.logger.normalLogEntry("Webserver start (" + info + ")");
		webServerInst.startContainer();

		Environment.periodicalServices = new PeriodicalServices();
	}

	public static void main(String[] arg) {
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

	public static void shutdown() {
		logger.normalLogEntry("Server is stopping ... ");
		Environment.shutdown();
		// webServerInst.stopContainer();
		System.exit(0);
	}
}
