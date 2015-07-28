package com.flabser.restful;

import com.flabser.users.VisibiltyType;

public class Application {
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

}