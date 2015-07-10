package com.flabser.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;

import com.flabser.appenv.AppEnv;
import com.flabser.exception.RuleException;
import com.flabser.rule.constants.RunMode;
import com.flabser.rule.page.PageRule;
import com.flabser.server.Server;

public class RuleProvider {
	public GlobalSetting global;

	private HashMap<String, PageRule> pageRuleMap = new HashMap<String, PageRule>();
	private AppEnv env;
	private Element root;

	public RuleProvider(AppEnv env) {
		try {
			this.env = env;
		} catch (Exception ne) {
			Server.logger.errorLogEntry(ne);
		}
	}

	public void initApp(String globalFileName) {
		loadGlobal(globalFileName);
	}

	public IRule getRule(String ruleID) throws RuleException {
		File docFile;

		ruleID = ruleID.toLowerCase();
		IRule rule = null;

		if (pageRuleMap.containsKey(ruleID)) {
			rule = pageRuleMap.get(ruleID);
		} else {
			docFile = new File(global.rulePath + File.separator + "Page" + File.separator + ruleID + ".xml");
			if (!docFile.exists()) {
				docFile = new File(global.primaryRulePath + File.separator + "Page" + File.separator + ruleID + ".xml");
			}
			PageRule pageRule = new PageRule(env, docFile);
			pageRuleMap.put(ruleID.toLowerCase(), pageRule);
			rule = pageRule;
		}

		rule.plusHit();
		return rule;

	}

	public Collection<PageRule> getPageRules(boolean reload) throws RuleException {
		if (reload) {
			pageRuleMap.clear();
			loadPages();
		}
		return pageRuleMap.values();

	}

	public boolean resetRule(int ruleType, String ruleID) {
		return false;

	}

	public boolean resetRules() {
		Server.logger.normalLogEntry("Reload application rules ...");

		pageRuleMap.clear();

		Server.logger.normalLogEntry("Application rules have been reloaded");
		return true;
	}

	private void loadGlobal(String globalFileName) {
		String globalPath = "rule" + File.separator + env.appType + File.separator + globalFileName;
		global = new GlobalSetting(globalPath, env);
	}

	private void loadPages() {
		try {
			File docFile = new File(global.rulePath + File.separator + "Page");
			if (!docFile.exists()) {
				docFile = new File(global.primaryRulePath + File.separator + "Page");
			}
			ArrayList<File> fl = getFiles(docFile);
			int n = 0;
			Document doc = null;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			while (n != fl.size()) {
				File file = null;
				try {
					file = fl.get(n);
					try {
						doc = db.parse(file.toString());
					} catch (SAXParseException e) {
						Server.logger.errorLogEntry("xml file structure error  file=" + file.getAbsolutePath()
								+ ", rule has not loaded");
					}
					root = doc.getDocumentElement();
					String attr = root.getAttribute("type");
					if (attr.equalsIgnoreCase("page")) {
						PageRule ruleObj = new PageRule(env, file);
						if (ruleObj.isOn != RunMode.ON) {
							Server.logger.verboseLogEntry("rule " + ruleObj.id + " turn off ");
						}

						pageRuleMap.put(ruleObj.id.toLowerCase(), ruleObj);
					}
				} catch (SAXParseException spe) {
					Server.logger.errorLogEntry("xml file structure error  file=" + file.getAbsolutePath()
							+ ", rule has not loaded");
					Server.logger.errorLogEntry(spe);
				} finally {
					n++;
				}

			}
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	private ArrayList<File> getFiles(File docFile) {
		ArrayList<File> fl = new ArrayList<File>();

		if (docFile.isDirectory()) {
			File[] list = docFile.listFiles();
			for (int i = list.length; --i >= 0;) {
				String name = list[i].getName().toLowerCase();
				if (name.endsWith(".xml")) {
					fl.add(list[i]);
				}
			}
		}
		return fl;
	}

}
