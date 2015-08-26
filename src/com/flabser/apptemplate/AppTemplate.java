package com.flabser.apptemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
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

public class AppTemplate implements ICache, _IContent {
	public boolean isValid;
	public String appType = "undefined";
	public RuleProvider ruleProvider;
	public HashMap<String, File> xsltFileMap = new HashMap<String, File>();
	public String adminXSLTPath;
	public GlobalSetting globalSetting;
	public Vocabulary vocabulary;


	private HashMap<String, _Page> cache = new HashMap<String, _Page>();
	private String docBase;

	public AppTemplate(String at) {
		isValid = true;
		appType = EnvConst.ADMIN_APP_NAME;
	}

	public AppTemplate(String appType, String globalFileName) {
		this.appType = appType;
		try {
			Server.logger.normalLogEntry("# init application template \"" + appType + "\"");
			ruleProvider = new RuleProvider(this);
			ruleProvider.initAppTemplate(globalFileName);
			globalSetting = ruleProvider.global;
			docBase = new File(Environment.primaryAppDir + "webapps/" + appType).getAbsolutePath();
			if (globalSetting.isOn == RunMode.ON) {
				if (globalSetting.langsList.size() > 0) {
					Server.logger.normalLogEntry("dictionary is loading...");

					try {
						Localizator l = new Localizator(globalSetting);
						vocabulary = l.populate("vocabulary");
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

	@Override
	public String toString() {
		return appType;
	}

	@Override
	public _Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException, WebFormValueException {
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
		return output.append("<entry><apptype>" + appType + "</apptype></entry>");
	}

	public String getDocBase() {
		return docBase;
	}

}
