package com.flabser.rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.apptemplate.WorkModeType;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.rule.constants.RunMode;
import com.flabser.server.Server;
import com.flabser.users.UserRoleCollection;
import com.flabser.util.XMLUtil;

public class GlobalSetting {

	public String rulePath;
	public String primaryRulePath;
	public RunMode isOn;
	public boolean isValid;
	public String entryPoint;
	public String defaultRedirectURL;
	public ArrayList<Lang> langsList = new ArrayList<Lang>();
	public String vocabulary = EnvConst.VOCABULARY_FILE;
	public UserRoleCollection roleCollection = new UserRoleCollection();
	private WorkModeType workMode;
	private String description;

	public GlobalSetting() {

	}

	public GlobalSetting(String path, AppTemplate env) {
		rulePath = "rule" + File.separator + env.templateType;
		primaryRulePath = Environment.primaryAppDir + rulePath;

		try {
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			doc = db.parse(path);

			if (XMLUtil.getTextContent(doc, "/rule/@mode").equalsIgnoreCase("on")) {
				isOn = RunMode.ON;
				isValid = true;
			}

			description = XMLUtil.getTextContent(doc, "/rule/description");
			if (description.equals("")) {
				description = env.templateType + "_description";
			}
			workMode = WorkModeType.valueOf(XMLUtil.getTextContent(doc, "/rule/workmode", true, "CLOUD", true));
			entryPoint = XMLUtil.getTextContent(doc, "/rule/entrypoint");

			defaultRedirectURL = XMLUtil.getTextContent(doc, "/rule/defaultredirecturl");
			if (defaultRedirectURL.equalsIgnoreCase("")) {
				defaultRedirectURL = "Error?type=default_url_not_defined";
			}

			NodeList langs = XMLUtil.getNodeList(doc, "/rule/langs/entry");
			for (int i = 0; i < langs.getLength(); i++) {
				Lang lang = new Lang(langs.item(i));
				if (lang.isOn == RunMode.ON) {
					langsList.add(lang);
				}
			}

			NodeList roles = XMLUtil.getNodeList(doc, "/rule/roles/entry");
			for (int i = 0; i < roles.getLength(); i++) {
				Role role = new Role(roles.item(i), env.templateType);

				if (role.isValid && role.isOn == RunMode.ON) {
					if (!role.name.equalsIgnoreCase("supervisor")) {
						roleCollection.put(role);
					} else {
						Server.logger.warningLogEntry(
								"A role name \"supervisor\" is reserved name of system roles. The role has not added to application");
					}
				}
			}

		} catch (FileNotFoundException fnfe) {
			Server.logger.errorLogEntry(fnfe.toString());
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	public WorkModeType getWorkMode() {
		return workMode;
	}

	public void setWorkMode(WorkModeType workMode) {
		this.workMode = workMode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
