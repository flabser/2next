package com.flabser.rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.Environment;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.constants.RuleType;
import com.flabser.rule.constants.RunMode;
import com.flabser.rule.page.ElementRule;
import com.flabser.rule.page.IElement;
import com.flabser.server.Server;
import com.flabser.servlets.PublishAsType;
import com.flabser.util.XMLUtil;

public abstract class Rule implements IElement, IRule {
	public RunMode isOn = RunMode.ON;
	public boolean isValid = true;
	public String description;
	public String id = "unknown";
	public String xsltFile;
	public PublishAsType publishAs = PublishAsType.XML;
	public String app;
	public Date lastUpdate = new Date();
	public Date regDate = new Date();
	public String filePath;
	public String scriptDirPath = "";
	public String primaryScriptDirPath;
	public int hits;
	public ArrayList<Caption> captions = new ArrayList<Caption>();
	public boolean addToHistory;
	public ArrayList<ElementRule> elements = new ArrayList<ElementRule>();
	protected org.w3c.dom.Document doc;
	protected RuleType type = RuleType.UNKNOWN;
	protected String targetDirectory = "libs";

	private boolean allowAnonymousAccess;

	protected Rule(AppTemplate env, File docFile) throws RuleException {
		try {
			DocumentBuilderFactory pageFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder pageBuilder = pageFactory.newDocumentBuilder();
			Document xmlFileDoc = pageBuilder.parse(docFile.toString());
			doc = xmlFileDoc;
			filePath = docFile.getAbsolutePath();
			scriptDirPath = env.globalSetting.rulePath + File.separator + "Resources" + File.separator + "scripts";
			primaryScriptDirPath = Environment.primaryAppDir + scriptDirPath;

			id = XMLUtil.getTextContent(doc, "/rule/@id",true);
			if (id.equals("")){
				id = FilenameUtils.removeExtension(docFile.getName());
			}
			Server.logger.verboseLogEntry("load rule: " + this.getClass().getSimpleName() + ", id=" + id);
			if (XMLUtil.getTextContent(doc, "/rule/@mode").equalsIgnoreCase("off")) {
				isOn = RunMode.OFF;
				isValid = false;
			}

			if (XMLUtil.getTextContent(doc, "/rule/@anonymous").equalsIgnoreCase("on")) {
				allowAnonymousAccess = true;
			}

			if (XMLUtil.getTextContent(doc, "/rule/@history").equalsIgnoreCase("on")) {
				addToHistory = true;
			}

			xsltFile = XMLUtil.getTextContent(doc, "/rule/xsltfile");
			if (!xsltFile.equals("")) {
				publishAs = PublishAsType.HTML;
			}
			xsltFile = "webapps" + File.separator + env.templateType + File.separator + "xsl" + File.separator + xsltFile;
			description = XMLUtil.getTextContent(doc, "/rule/description");

			NodeList captionList = XMLUtil.getNodeList(doc, "/rule/caption");
			for (int i = 0; i < captionList.getLength(); i++) {
				Caption c = new Caption(captionList.item(i));
				if (c.isOn == RunMode.ON) {
					captions.add(c);
				}
			}

		} catch (SAXParseException spe) {
			Server.logger.errorLogEntry("XML-file structure error (" + docFile.getAbsolutePath() + ")");
			Server.logger.errorLogEntry(spe);
		} catch (FileNotFoundException e) {
			throw new RuleException("Rule \"" + docFile.getAbsolutePath() + "\" has not found");
		} catch (ParserConfigurationException e) {
			Server.logger.errorLogEntry(e);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} catch (SAXException se) {
			Server.logger.errorLogEntry(se);
		}

	}


	protected void setIsOn(String isOnAsText) {
		if (isOnAsText.equalsIgnoreCase("on")) {
			isOn = RunMode.ON;
		} else {
			isOn = RunMode.OFF;
		}
	}

	protected void setDescription(String d) {
		description = d;
	}

	protected void setID(String id) {
		this.id = id;
	}

	protected void setCaptions(String[] id) {

	}

	@Override
	public boolean isAnonymousAllowed() {
		return allowAnonymousAccess;
	}

	@Override
	public void plusHit() {
		hits++;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " id=" + id + ", file=" + filePath;
	}

	public String getXSLT() {
		return xsltFile.replace("\\", File.separator);
	}

	public String getAsXML() {
		return "";
	}

	@Override
	public String getRuleAsXML(String app) {
		String xmlText = "<rule id=\"" + id + "\" isvalid=\"" + isValid + "\" app=\"" + app + "\" ison=\"" + isOn + "\">" + "<description>" + description
				+ "</description>";
		return xmlText + "</fields></rule>";
	}

	@Override
	public boolean addToHistory() {
		return addToHistory;
	}

	@Override
	abstract public void update(Map<String, String[]> fields) throws WebFormValueException;

	@Override
	abstract public boolean save();

	@Override
	public String getRuleID() {
		return type + "_" + id;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getScriptDirPath() {
		return scriptDirPath;
	}

	@Override
	public String getPrimaryScriptDirPath() {
		return primaryScriptDirPath;
	}

	@Override
	public ArrayList<Caption> getCaptions() {
		return captions;
	}

}
