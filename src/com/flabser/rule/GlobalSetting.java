package com.flabser.rule;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.rule.constants.RunMode;
import com.flabser.users.UserRoleCollection;
import com.flabser.util.XMLUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.util.ArrayList;


public class GlobalSetting {
	public String description;
	public String rulePath;
	public String primaryRulePath;
	public String appName;
	public String implementation;
	public RunMode isOn;	
	public boolean isValid;
	public String entryPoint;
	public String defaultRedirectURL;
	public ArrayList <Lang> langsList = new ArrayList <Lang>();
	public boolean multiLangEnable;
	public Lang primaryLang;	
	public String vocabulary = "vocabulary.xml";
	public UserRoleCollection roleCollection = new UserRoleCollection();
	
	
		
	public GlobalSetting(String path, AppEnv env){	
		rulePath = "rule" + File.separator + env.appType;
		primaryRulePath = Environment.primaryAppDir + rulePath;
		
		try {
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;

			db = dbf.newDocumentBuilder();
			doc = db.parse(path);
		
			if ( XMLUtil.getTextContent(doc, "/rule/@mode").equalsIgnoreCase("on") ) {
				isOn = RunMode.ON;
				isValid = true;
			}

			description = XMLUtil.getTextContent(doc, "/rule/description");
			appName = XMLUtil.getTextContent(doc, "/rule/appname");
			implementation = XMLUtil.getTextContent(doc, "/rule/impl");
			
			entryPoint = XMLUtil.getTextContent(doc, "/rule/entrypoint");
			
			defaultRedirectURL = XMLUtil.getTextContent(doc, "/rule/defaultredirecturl");
			if(defaultRedirectURL.equalsIgnoreCase("")){
				defaultRedirectURL = "Error?type=default_url_not_defined";
			}
			
			NodeList langs = XMLUtil.getNodeList(doc, "/rule/langs/entry");
			for (int i = 0; i < langs.getLength(); i++) {
				Lang lang = new Lang(langs.item(i));
				if ( lang.isOn == RunMode.ON ) {
					langsList.add(lang);
					if (lang.isPrimary){
						primaryLang = lang;
					}
				}
			}

			if (langsList.size() > 1) multiLangEnable = true;

		
			


			NodeList roles = XMLUtil.getNodeList(doc, "/rule/roles/entry");
			for (int i = 0; i < roles.getLength(); i++) {
				Role role = new Role(roles.item(i), appName);
				
				if (role.isValid && role.isOn == RunMode.ON) {
					if (!role.name.equalsIgnoreCase("supervisor")){
						roleCollection.put(role);
					}else{
						AppEnv.logger.warningLogEntry("A role name \"supervisor\" is reserved name of system roles. The role has not added to application");
					}
				}
			}		
			
		} catch (FileNotFoundException fnfe) {
			AppEnv.logger.errorLogEntry(fnfe.toString());
		} catch (Exception e) {
			AppEnv.logger.errorLogEntry(e);
		}
	}
}
