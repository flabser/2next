package com.flabser.env;

import java.io.File;

import org.apache.catalina.Host;
import org.apache.catalina.core.StandardHost;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.localization.LanguageType;
import com.flabser.script._Exception;
import com.flabser.script._IContent;

public class Site implements _IContent {
	private String virtualHostName;
	private String appBase;
	private String global = "global.xml";
	private String parent;
	private Host virtualHost;
	private AppTemplate appTemlate;

	public String getVirtualHostName() {
		return virtualHostName;
	}

	public void setVirtualHostName(String virtualHostName) {
		this.virtualHostName = virtualHostName;
	}

	public String getAppBase() {
		return appBase;
	}

	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}

	public String getGlobal() {
		return global;
	}

	public void setGlobal(String global) {
		this.global = global;
	}

	public void setParent(String parent) {
		this.parent = parent;

	}

	public String getParent() {
		return parent;

	}

	public Host getHost() {
		if (virtualHost == null) {
			virtualHost = new StandardHost();
			virtualHost.setName(virtualHostName);
			return virtualHost;
		} else {
			return virtualHost;
		}

	}

	public AppTemplate getAppTemlate() {
		return appTemlate;
	}

	public void setAppTemlate(AppTemplate appTemlate) {
		this.appTemlate = appTemlate;
	}

	@Override
	public StringBuffer toXML(String lang) throws _Exception {
		StringBuffer output = new StringBuffer(1000);
		String sc = appTemlate.vocabulary.getWord(appTemlate.globalSetting.getDescription(),
				LanguageType.valueOf(lang));
		return output.append("<apptype>" + appBase + "</apptype><description>" + sc + "</description>");
	}

	public String getFullPathAppBase() {
		return new File(Environment.primaryAppDir + "webapps/" + appBase).getAbsolutePath();
	}

	@Override
	public String toString() {
		return "[appBase=\"" + appBase + "\", virtualHostName=\"" + virtualHostName + "\" , parent=\"" + parent + "\"]";
	}

}
