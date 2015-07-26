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
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.scheduler.PeriodicalServices;
import com.flabser.script._Page;
import com.flabser.server.Server;
import com.flabser.server.WebServer;
import com.flabser.util.XMLUtil;

public class Environment implements ICache {
	public static int serverVersion;
	public static String serverName;
	public static String hostName;
	public static int httpPort = 38779;
	public static String httpSchema = WebServer.httpSchema;

	public static ISystemDatabase systemBase;
	public static String defaultSender = "";
	public static HashMap<String, String> mimeHash = new HashMap<String, String>();
	public static String primaryAppDir;
	public static HashMap<String, Site> webAppToStart = new HashMap<String, Site>();
	public static String tmpDir;
	public static String libsDir;
	public static ArrayList<String> fileToDelete = new ArrayList<String>();
	// public static ILogger logger;
	public static PeriodicalServices periodicalServices;

	public static Boolean isTLSEnable = false;
	public static int secureHttpPort;
	public static String certFile = "";
	public static String certKeyFile = "";

	public static String smtpPort;
	public static boolean smtpAuth;
	public static String SMTPHost;
	public static String smtpUser;
	public static String smtpPassword;
	public static Boolean mailEnable = false;
	private static String defaultRedirectURL;
	private static HashMap<String, AppEnv> applications = new HashMap<String, AppEnv>();
	private static HashMap<String, Object> cache = new HashMap<String, Object>();

	public static void init() {
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

			Server.logger.normalLogEntry("Initialize runtime environment");
			initMimeTypes();

			hostName = XMLUtil.getTextContent(xmlDocument, "/tn/hostname");
			if (hostName.trim().equals("")) {
				hostName = getHostName();
			}

			serverName = XMLUtil.getTextContent(xmlDocument, "/tn/name");
			String portAsText = XMLUtil.getTextContent(xmlDocument, "/tn/port");
			try {
				httpPort = Integer.parseInt(portAsText);
				Server.logger.normalLogEntry("WebServer is going to use port: " + httpPort);
			} catch (NumberFormatException nfe) {
				Server.logger.normalLogEntry("WebServer is going to use standart port");
			}

			primaryAppDir = XMLUtil.getTextContent(xmlDocument, "/tn/primaryappdir");
			if (!primaryAppDir.equalsIgnoreCase("")) {
				primaryAppDir = primaryAppDir + File.separator;
			}

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
				isTLSEnable = XMLUtil.getTextContent(xmlDocument, "/tn/tls/@mode").equalsIgnoreCase("on");
				if (isTLSEnable) {
					String tlsPort = XMLUtil.getTextContent(xmlDocument, "/tn/tls/port");
					try {
						secureHttpPort = Integer.parseInt(tlsPort);
					} catch (NumberFormatException nfe) {
						secureHttpPort = 38789;
					}
					certFile = XMLUtil.getTextContent(xmlDocument, "/tn/tls/certfile");
					certKeyFile = XMLUtil.getTextContent(xmlDocument, "/tn/tls/certkeyfile");

					Server.logger.normalLogEntry("TLS is enabled");
					httpSchema = WebServer.httpSecureSchema;
					httpPort = secureHttpPort;
				}
			} catch (Exception ex) {
				Server.logger.normalLogEntry("TLS configuration error");
				isTLSEnable = false;
				certFile = "";
				certKeyFile = "";
			}

			try {
				mailEnable = (XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/@mode").equalsIgnoreCase("on")) ? true : false;
				if (mailEnable) {
					SMTPHost = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtphost");
					defaultSender = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/defaultsender");
					smtpAuth = Boolean.valueOf(XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/auth"));
					smtpUser = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpuser");
					smtpPassword = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtppassword");
					smtpPort = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpport");
					Server.logger.normalLogEntry("MailAgent is going to redirect some messages to host: " + SMTPHost);
				} else {
					Server.logger.normalLogEntry("MailAgent is switch off");
				}
			} catch (NumberFormatException nfe) {
				Server.logger.normalLogEntry("MailAgent is not set");
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

		} catch (SAXException se) {
			Server.logger.errorLogEntry(se);
		} catch (ParserConfigurationException pce) {
			Server.logger.errorLogEntry(pce);
		} catch (IOException ioe) {
			Server.logger.errorLogEntry(ioe);
		}
	}

	public static void addApplication(AppEnv env) {
		applications.put(env.appType, env);
	}

	public static AppEnv getApplication(String appID) {
		return applications.get(appID);
	}

	public static Collection<AppEnv> getApplications() {
		return applications.values();
	}

	public static String getFullHostName() {
		return httpSchema + "://" + Environment.hostName + ":" + Environment.httpPort;
	}

	public static String getDefaultRedirectURL() {
		return defaultRedirectURL;
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
			Server.logger.errorLogEntry(e);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} catch (ParserConfigurationException e) {
			Server.logger.errorLogEntry(e);
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
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException, WebFormValueException {
		Object obj = cache.get(page.getID());
		String cacheParam = formData.get("cache")[0];
		if (obj == null || cacheParam.equalsIgnoreCase("reload")) {
			_Page buffer = page.getContent(formData);
			cache.put(page.getID(), buffer);
			return buffer;
		} else {
			return (_Page) obj;
		}

	}

	@Override
	public void flush() {
		cache.clear();
	}

	public static void shutdown() {
		periodicalServices.stop();
	}

}
