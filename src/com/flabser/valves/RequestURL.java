package com.flabser.valves;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {
	private String appName = "";
	private String appID = "";
	private String url;

	public RequestURL(String url) {
		this.url = url;
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)(/[\\p{Lower}0-9]{16})?(/.*)*$");
		Matcher matcher = pattern.matcher(url != null ? url.trim() : "");
		if (matcher.matches()) {
			appName = matcher.group(1) == null ? "" : matcher.group(1);
			appID = matcher.group(2) == null ? "" : matcher.group(2).substring(1);
		}
	}

	public String getAppName() {
		return appName;
	}

	public String getAppID() {
		return appID;
	}

	@Deprecated
	public boolean isWebResource() {
		// TODO Need to improve
		if (appName.equals("SharedResources") || url.contains("js") || url.contains("css")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDefault() {
		// TODO Need to improve
		if (url.equals("/")) {
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	public boolean isTemplate() {
		// TODO Need to improve
		if (url.equals("/CashTracker/") || url.equals("/Nubis")) {
			return true;
		} else {
			return false;
		}
	}

	@Deprecated
	public boolean isAuth() {
		// TODO Need to improve
		if (url.contains("session") || url.startsWith("/Nubis/Provider") || url.equals("/CashTracker/Provider?id=login")) {
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

	public boolean isAuthRequest() {
		return false;
	}

	public boolean isPage() {
		return false;
	}

	public boolean isAnonymousPage() {
		return false;
	}

	public String getUrl() {
		return url;
	}

	public boolean isProtected() {
		return false;
	}

}
