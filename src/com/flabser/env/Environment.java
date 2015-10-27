package com.flabser.env;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.Localizator;
import com.flabser.localization.LocalizatorException;
import com.flabser.localization.Vocabulary;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.scheduler.PeriodicalServices;
import com.flabser.script._Page;
import com.flabser.server.Server;
import com.flabser.util.XMLUtil;

public class Environment implements ICache {
	public static int serverVersion;
	public static String hostName;
	public static int httpPort = EnvConst.DEFAULT_HTTP_PORT;


	public static ISystemDatabase systemBase;
	public static String defaultSender = "";
	public static String primaryAppDir;
	public static HashMap<String, Site> availableTemplates = new HashMap<String, Site>();
	public static String tmpDir;
	public static String libsDir;

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
	public static String workspaceName;
	public static Vocabulary vocabulary;

	private static HashMap<String, Object> cache = new HashMap<String, Object>();


	public static void init() {
		loadProperties();
		initProcess();
	}

	private static void initProcess() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			SAXParser saxParser = factory.newSAXParser();
			SAXHandler cfgXMLhandler = new SAXHandler();
			File file = new File(EnvConst.CFG_FILE);
			saxParser.parse(file, cfgXMLhandler);
			Document xmlDocument = getDocument();

			Server.logger.infoLogEntry("initialize runtime environment");

			hostName = XMLUtil.getTextContent(xmlDocument, "/tn/hostname");
			if (hostName.trim().equals("")) {
				hostName = getHostName();
			}

			String portAsText = XMLUtil.getTextContent(xmlDocument, "/tn/port");
			try {
				httpPort = Integer.parseInt(portAsText);
				Server.logger.infoLogEntry("webServer will use port: " + httpPort);
			} catch (NumberFormatException nfe) {
				Server.logger.infoLogEntry("webServer will use standart port");
			}

			primaryAppDir = XMLUtil.getTextContent(xmlDocument, "/tn/primaryappdir");
			if (!primaryAppDir.equalsIgnoreCase("")) {
				primaryAppDir = primaryAppDir + File.separator;
			}

			Localizator l = new Localizator();
			String vocabuarFilePath = "resources" + File.separator + "vocabulary.xml";
			vocabulary = l.populate(vocabuarFilePath);

			workspaceName = XMLUtil.getTextContent(xmlDocument, "/tn/applications/@workspace", false, "", true);

			NodeList nodeList = XMLUtil.getNodeList(xmlDocument, "/tn/applications");
			if (nodeList.getLength() > 0) {
				org.w3c.dom.Element root = xmlDocument.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("app");
				for (int i = 0; i < nodes.getLength(); i++) {
					Node appNode = nodes.item(i);
					if (XMLUtil.getTextContent(appNode, "name/@mode", false).equals("on")) {
						String appName = XMLUtil.getTextContent(appNode, "name", false);
						Site site = new Site();
						site.setAppBase(appName);
						String vh = XMLUtil.getTextContent(appNode, "name/@sitename", false);
						site.setVirtualHostName(vh);
						if (vh.equals("")) {
							site.setParent(XMLUtil.getTextContent(appNode, "name/@parent", false));
						}else{
							site.setParent("");
						}

						String globalAttrValue = XMLUtil.getTextContent(appNode, "name/@global", false);
						if (!globalAttrValue.equals("")) {
							site.setGlobal(globalAttrValue);
						}

						availableTemplates.put(appName, site);
						if(!site.getVirtualHostName().equals("")) {
							availableTemplates.put(site.getVirtualHostName(), site);
						}
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

					Server.logger.infoLogEntry("TLS is enabled");
					httpPort = secureHttpPort;
				}
			} catch (Exception ex) {
				Server.logger.infoLogEntry("TLS configuration error");
				isTLSEnable = false;
				certFile = "";
				certKeyFile = "";
			}

			try {
				mailEnable = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/@mode").equalsIgnoreCase("on") ? true
						: false;
				if (mailEnable) {
					SMTPHost = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtphost");
					defaultSender = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/defaultsender");
					smtpAuth = Boolean.valueOf(XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/auth"));
					smtpUser = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpuser");
					smtpPassword = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtppassword");
					smtpPort = XMLUtil.getTextContent(xmlDocument, "/tn/mailagent/smtpport");
					Server.logger.infoLogEntry("mailAgent will redirect some messages to host: " + SMTPHost);
				} else {
					Server.logger.infoLogEntry("mailAgent is switch off");
				}
			} catch (NumberFormatException nfe) {
				Server.logger.infoLogEntry("mailAgent is not set");
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
		} catch (LocalizatorException e) {
			Server.logger.errorLogEntry(e);
		}
	}

	private static void loadProperties(){
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("resources" + File.separator + "config.properties");
			prop.load(input);
			Field[] declaredFields = EnvConst.class.getDeclaredFields();
			for (Field field : declaredFields) {
				if (Modifier.isStatic(field.getModifiers())) {
					String value = prop.getProperty(field.getName());
					if (value != null) {
						field.set(String.class, prop.getProperty(field.getName()));
					}
				}
			}
		}catch(Exception e){
			//	e.printStackTrace();

		}
	}

	public static String getWorkspaceName() {
		return workspaceName;
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
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException,
	WebFormValueException {
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
		if (periodicalServices != null) {
			periodicalServices.stop();
		}
	}

}
