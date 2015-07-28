package com.flabser.restful;

import com.flabser.script._IContent;
import com.flabser.users.VisibiltyType;

public class Application implements _IContent {
	public String appID;
	public String appName;
	public String owner;
	public String defaultURL;
	VisibiltyType visibilty;
	public String appType;

	public String getDefaultURL() {
		return defaultURL;
	}

	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
	}

	public String getAppType() {
		return appType;
	}

	public String getAppID() {
		return appID;
	}

	public String getAppName() {
		return appName;
	}

	public String getOwner() {
		return owner;
	}

	public VisibiltyType getVisibilty() {
		return visibilty;
	}

	@Override
	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<apptype>" + appType + "</apptype><appname>" + appName + "</appname><owner>" + owner + "</owner><appid>" + appID + "</appid>");
	}

}