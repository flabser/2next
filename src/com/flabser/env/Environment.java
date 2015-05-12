package com.flabser.env;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.log.ILogger;
import com.flabser.rule.constants.RunMode;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.server.Server;
import com.flabser.util.XMLUtil;


public class Environment implements Const, ICache {
	public static int serverVersion;
	public static String serverName;
	public static String hostName;
	public static int httpPort = 38779;
	public static boolean noWSAuth = false;
	public static String httpSchema = "http";

	public static ISystemDatabase systemBase;
	public static String defaultSender = "";
	public static HashMap <String, String> mimeHash = new HashMap <String, String>();
	public static boolean adminConsoleEnable;
	public static String primaryAppDir;
	public static HashMap <String, Site> webAppToStart = new HashMap <String, Site>();
	public static String tmpDir;
	public static String libsDir;
	public static ArrayList <String> fileToDelete = new ArrayList <String>();
	public static ILogger logger;
	public static int delaySchedulerStart;

	public static Boolean isSSLEnable = false;
	public static int secureHttpPort;
	public static String keyPwd = "";
	public static String keyStore = "";
	public static String trustStore;
	public static String trustStorePwd;
	public static boolean isClientSSLAuthEnable;


	public static String smtpPort;
	public static boolean smtpAuth;
	public static String SMTPHost;
	public static String smtpUser;
	public static String smtpPassword;
	public static Boolean mailEnable = false;
	private static String defaultRedirectURL;
	public static RunMode debugMode = RunMode.OFF;
	private static HashMap <String, AppEnv> applications = new HashMap <String, AppEnv>();
	private static HashMap <String, Object> cache = new HashMap <String, Object>();
	private static ArrayList <IDatabase> delayedStart = new ArrayList <IDatabase>();
	public static String backupDir;

	public static void init() {
		logger = Server.logger;
		initProcess();
	}

	private static void initProcess() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			SAXParser saxParser = factory.newSAXParser();
			SAXHandler cfgXMLhandler = new SAXHandler();
			File file = new File("cfg.xml");
			saxParser.parse(file, cfgXMLhandler);
			Document xmlDocument = getDocument();

			logger.normalLogEntry("Initialize runtime environment");
			initMimeTypes();

			hostName = XMLUtil.getTextContent(xmlDocument, "/tn/hostname");
			if (hostName.trim().equals("")) {
				hostName = getHostName();
			}

			serverName = XMLUtil.getTextContent(xmlDocument, "/tn/name");
			String portAsText = XMLUtil.getTextContent(xmlDocument, "/tn/port");
			try {
				httpPort = Integer.parseInt(portAsText);
				logger.normalLogEntry("WebServer is going to use port: " + httpPort);
			} catch (NumberFormatException nfe) {
				logger.normalLogEntry("WebServer is going to use standart port");
			}

			try {
				String auth = XMLUtil.getTextContent(xmlDocument, "/tn/no-ws-auth");
				if ("true".equalsIgnoreCase(auth)) {
					noWSAuth = true;
				}
			} catch (Exception e) {
				noWSAuth = false;
			}

			try {
				if (XMLUtil.getTextContent(xmlDocument, "/tn/adminapp/@mode").equalsIgnoreCase("on")) {
					adminConsoleEnable = true;
				}
			} catch (Exception nfe) {
				adminConsoleEnable = false;
			}

			try {
				delaySchedulerStart = Integer.parseInt(XMLUtil.getTextContent(xmlDocument,
						"/tn/scheduler/startdelaymin"));
			} catch (Exception nfe) {
				delaySchedulerStart = 1;
			}
			
			primaryAppDir = XMLUtil.getTextContent(xmlDocument, "/tn/primaryappdir");
			if (!primaryAppDir.equalsIgnoreCase("")) primaryAppDir = primaryAppDir + File.separator;
						
	
			defaultRedirectURL = "/" + XMLUtil.getTextContent(xmlDocument, "/tn/applications/@default", false, "Workspace", true);

			NodeList nodeList = XMLUtil.getNodeList(xmlDocument, "/tn/applications");
			if (nodeList.getLength() > 0) {
				org.w3c.dom.Element root = xmlDocument.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("app");
				for (int i = 0; i < nodes.getLength(); i++) {
					Node appNode = nodes.item(i);
					if (XMLUtil.getTextContent(appNode, "name/@mode", false).equals("on")) {
						String appName = XMLUtil.getTextContent(appNode, "name", false);
						Site site = new Site();
						site.appBase = appName;						
						site.name = XMLUtil.getTextContent(appNode, "name/@sitename", false);
						String globalAttrValue = XMLUtil.getTextContent(appNode, "name/@global", false);
						if (!globalAttrValue.equals("")) {
							site.global = globalAttrValue;
						}
						webAppToStart.put(appName, site);
					}
				}
			}

			try {
				isSSLEnable = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/@mode").equalsIgnoreCase("on");
				if (isSSLEnable) {
					String sslPort = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/port");
					try {
						secureHttpPort = Integer.parseInt(sslPort);
					} catch (NumberFormatException nfe) {
						secureHttpPort = 38789;
					}
					keyPwd = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/keypass");
					keyStore = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/keystore");
					isClientSSLAuthEnable = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/clientauth/@mode")
							.equalsIgnoreCase("on");
					if (isClientSSLAuthEnable) {
						trustStore = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/clientauth/truststorefile");
						trustStorePwd = XMLUtil.getTextContent(xmlDocument, "/tn/ssl/clientauth/truststorepass");
					}
					// logger.normalLogEntry("SSL is enabled. keyPass: " + keyPwd +", keyStore:" +
					// keyStore);
					logger.normalLogEntry("TLS is enabled");
					httpSchema = "https";
				}
			} catch (Exception ex) {
				logger.normalLogEntry("TLS configiration error");
				isSSLEnable = false;
				keyPwd = "";
				keyStore = "";
			}

			try {
				mailEnable = (XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/@mode").equalsIgnoreCase("on")) ? true
						: false;
				if (mailEnable) {
					SMTPHost = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtphost");
					defaultSender = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/defaultsender");
					smtpAuth = Boolean.valueOf(XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/auth"));
					smtpUser = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpuser");
					smtpPassword = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtppassword");
					smtpPort = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpport");
					logger.normalLogEntry("MailAgent is going to redirect some messages to host: " + SMTPHost);
				} else {
					logger.normalLogEntry("MailAgent is switch off");
				}
			} catch (NumberFormatException nfe) {
				logger.normalLogEntry("MailAgent is not set");
				SMTPHost = "";
				defaultSender = "";
			}

			

			File tmp = new File("tmp");
			if (!tmp.exists()) {
				tmp.mkdir();
			}

			tmpDir = tmp.getAbsolutePath();
			
			File libs = new File("bin");
			if (!libs.exists()) {
				tmp.mkdir();
			}

			libsDir = libs.getAbsolutePath();

			File backup = new File("backup");
			if (!backup.exists()) {
				backup.mkdir();
			}

			backupDir = backup.getAbsolutePath();

			

			if (XMLUtil.getTextContent(xmlDocument, "/tn/debug/@mode").equalsIgnoreCase("on")) {
				debugMode = RunMode.ON;
			}

			

			

			{
				// BackupServiceRule bsr = new BackupServiceRule();
				// bsr.init(new Environment());
				// try{
				// Class c = Class.forName(bsr.getClassName());
				// IDaemon daemon = (IDaemon)c.newInstance();
				// daemon.init(bsr);
				// scheduler.addProcess(bsr, daemon);
				// }catch (InstantiationException e) {
				// logger.errorLogEntry(e);
				// } catch (IllegalAccessException e) {
				// logger.errorLogEntry(e);
				// } catch (ClassNotFoundException e) {
				// logger.errorLogEntry(e);
				// }
			}
		} catch (SAXException se) {
			logger.errorLogEntry(se);
		} catch (ParserConfigurationException pce) {
			logger.errorLogEntry(pce);
		} catch (IOException ioe) {
			logger.errorLogEntry(ioe);
		}
	}

	
	public static void addApplication(AppEnv env) {
		applications.put(env.appType, env);		
	}

	public static void addDelayedInit(IDatabase db) {
		delayedStart.add(db);
	}

	public static AppEnv getApplication(String appID) {
		return applications.get(appID);
	}
	
	public static Collection <AppEnv> getApplications() {
		return applications.values();
	}

	public static String getFullHostName() {
		return httpSchema + "://" + Environment.hostName + ":" + Environment.httpPort;
	}

	public static String getDefaultRedirectURL() {
		return defaultRedirectURL;
	}

	public static String getWorkspaceURL() {
		return "Workspace";
	}



	private static void initMimeTypes() {
		mimeHash.put("pdf", "application/pdf");
		mimeHash.put("doc", "application/msword");
		mimeHash.put("xls", "application/vnd.ms-excel");
		mimeHash.put("tif", "image/tiff");
		mimeHash.put("rtf", "application/msword");
		mimeHash.put("gif", "image/gif");
		mimeHash.put("jpg", "image/jpeg");
		mimeHash.put("html", "text/html");
		mimeHash.put("zip", "application/zip");
		mimeHash.put("rar", "application/x-rar-compressed");
	}

	private static Document getDocument() {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;

			builder = domFactory.newDocumentBuilder();
			return builder.parse("cfg.xml");
		} catch (SAXException e) {
			logger.errorLogEntry(e);
		} catch (IOException e) {
			logger.errorLogEntry(e);
		} catch (ParserConfigurationException e) {
			logger.errorLogEntry(e);
		}
		return null;
	}

	private static String getHostName() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return addr.getHostName();
	}

	@Override
	public StringBuffer getPage(Page page, Map <String, String[]> formData) throws ClassNotFoundException, RuleException{
		Object obj = cache.get(page.getID());
		String cacheParam = formData.get("cache")[0];
		if (obj == null || cacheParam.equalsIgnoreCase("reload")) {
			StringBuffer buffer = page.getContent(formData);
			cache.put(page.getID(), buffer);
			return buffer;
		} else {
			return (StringBuffer) obj;
		}

	}

	@Override
	public void flush() {
		cache.clear();
	}

	public static void shutdown() {
		
	}


}
