package com.flabser.apptemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.env.Site;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.Localizator;
import com.flabser.localization.LocalizatorException;
import com.flabser.localization.Vocabulary;
import com.flabser.rule.GlobalSetting;
import com.flabser.rule.RuleProvider;
import com.flabser.rule.constants.RunMode;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.script._IContent;
import com.flabser.script._Page;
import com.flabser.server.Server;
import com.flabser.server.WebServer;

public class AppTemplate implements ICache, _IContent {
	public boolean isValid;

	public RuleProvider ruleProvider;
	public HashMap<String, File> xsltFileMap = new HashMap<String, File>();
	public String adminXSLTPath;
	public GlobalSetting globalSetting;
	public Vocabulary vocabulary;

	public String templateType = "undefined";
	private HashMap<String, _Page> cache = new HashMap<String, _Page>();
	private String url;

	private AppTemplate parent;

	public AppTemplate(String at) {
		isValid = true;
		templateType = EnvConst.ADMIN_APP_NAME;
	}

	@Deprecated
	public AppTemplate(String appType, String globalFileName) {
		this.templateType = appType;
		try {
			Server.logger.normalLogEntry("# init application template \"" + appType + "\"");
			ruleProvider = new RuleProvider(this);
			ruleProvider.initAppTemplate(globalFileName);
			globalSetting = ruleProvider.global;
			if (globalSetting.isOn == RunMode.ON) {
				if (globalSetting.langsList.size() > 0) {
					Server.logger.normalLogEntry("dictionary is loading...");

					try {
						Localizator l = new Localizator();
						String vocabuarFilePath = globalSetting.rulePath + File.separator + "Resources" + File.separator + "vocabulary.xml";
						vocabulary = l.populate(vocabuarFilePath);
						if (vocabulary != null) {
							Server.logger.normalLogEntry("dictionary has loaded");
						}
					} catch (LocalizatorException le) {
						Server.logger.verboseLogEntry(le.getMessage());
					}

				}

				isValid = true;
			} else {
				Server.logger.warningLogEntry("application: \"" + appType + "\" is off");

			}

		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	public AppTemplate(Site site) {
		templateType = site.getAppBase();
		if (!site.getParent().equals("")) {
			Site parentSite = Environment.availableTemplates.get(site.getParent());
			parent = parentSite.getAppTemlate();
		}
		try {

			ruleProvider = new RuleProvider(this);
			ruleProvider.initAppTemplate(site.getGlobal());
			globalSetting = ruleProvider.global;
			String t = "";
			if (globalSetting.getWorkMode() == WorkModeType.CLOUD) {
				t = " template";
			}
			Server.logger.normalLogEntry("# init" + t + " \"" + templateType + "\"");
			if (globalSetting.isOn == RunMode.ON) {
				if (globalSetting.langsList.size() > 0) {
					Server.logger.normalLogEntry("dictionary is loading...");

					try {
						Localizator l = new Localizator();
						String vocabuarFilePath = globalSetting.rulePath + File.separator + "Resources" + File.separator + "vocabulary.xml";
						vocabulary = l.populate(vocabuarFilePath);
						if (vocabulary != null) {
							Server.logger.normalLogEntry("dictionary has loaded");
						}
					} catch (LocalizatorException le) {
						Server.logger.verboseLogEntry(le.getMessage());
					}

				}

				if (site.getVirtualHostName().equals("")) {
					url = WebServer.httpSchema + "://" + Environment.hostName + ":" + Environment.httpPort + "/"
							+ templateType;
				} else {
					url = WebServer.httpSchema + "://" + site.getVirtualHostName() + ":" + Environment.httpPort;
				}

				isValid = true;
			} else {
				Server.logger.warningLogEntry("application: \"" + templateType + "\" is off");

			}

		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	public String getUrl() {
		return url;
	}

	public String getWorkspaceURL() {
		if (parent != null) {
			return parent.getUrl() + "/Provider?id=ws";
		} else {
			Site site = Environment.availableTemplates.get(Environment.getWorkspaceName());
			return site.getAppTemlate().url + "/Provider?id=ws";
		}
	}

	public String getLoginURL() {
		if (parent != null) {
			return parent.getUrl() + "/Provider?id=login";
		} else {
			return url + "/Provider?id=login";
		}
	}

	@Override
	public String toString() {
		return templateType;
	}

	@Override
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException,
	WebFormValueException {
		boolean reload = false;
		Object obj = cache.get(page.getID());
		String p[] = formData.get("cache");
		if (p != null) {
			String cacheParam = formData.get("cache")[0];
			if (cacheParam.equalsIgnoreCase("reload")) {
				reload = true;
			}
		}
		if (obj == null || reload) {
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

	@Override
	public StringBuffer toXML() throws _Exception {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry><apptype>" + templateType + "</apptype></entry>");
	}

}
