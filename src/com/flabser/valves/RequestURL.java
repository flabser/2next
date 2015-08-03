package com.flabser.valves;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {
	private String appName = "";
	private String appID = "";
	private String url;
	public final static int SERVLET = 11;
	public final static int REST = 12;

	public RequestURL(String url) {
		this.url = url;
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)(/_[\\p{Lower}0-9]{16}_)?.*$");
		Matcher matcher = pattern.matcher(url != null ? url.trim() : "");
		if (matcher.matches()) {
			appName = matcher.group(1) == null ? "" : matcher.group(1);
			appID = matcher.group(2) == null ? "" : matcher.group(2).substring(2, matcher.group(2).length() - 1);
		}
	}

	public String getAppName() {
		return isSharedResource() ? "" : appName;
	}

	public String getAppID() {
		return appID;
	}

	@Deprecated
	public boolean isWebResource() {
		// TODO Need to improve
		if (appName.equalsIgnoreCase("SharedResources") || url.contains("js") || url.contains("css")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDefault() {
		return url.matches("/" + appName + "(/(Provider)?)?/?") || url.trim().equals("");
	}

	@Deprecated
	public boolean isTemplate() {
		// TODO Need to improve
		if (url.equalsIgnoreCase("/CashTracker/") || url.equalsIgnoreCase("/Nubis")) {
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	public boolean isAuth() {
		// TODO Need to improve
		if (url.contains("session") || url.equalsIgnoreCase("/Nubis/Provider") || url.equalsIgnoreCase("/CashTracker/Provider?type=page&id=login")) {
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	public boolean isServerMessage() {
		// TODO Need to improve
		if (url.contains("error")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSimpleObject() {
		return url.matches(".*/\\w+\\.\\w+$");
	}

	public boolean isAuthRequest() {
		return url.matches(".+/session$");
	}

	public boolean isPage() {
		return url.matches(".*/Provider\\?(\\w+=\\w+)(&\\w+=\\w+)*") || url.matches(".*/page/[\\w\\.]+");
	}

	public boolean isSharedResource() {
		return appName.equalsIgnoreCase("SharedResources");
	}

	public boolean isAnonymousPage() {
		return false;
	}

	public String getUrl() {
		return url;
	}

}
