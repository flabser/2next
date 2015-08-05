package com.flabser.valves;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {
	private String appName = "";
	private String appID = "";
	private String url;

	public RequestURL(String url) {
		this.url = url;
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)(/[\\p{Lower}0-9]{16})?.*$");
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

	public boolean isDefault() {
		return url.matches("/" + appName + "(/(Provider)?)?/?") || url.trim().equals("");
	}

	public boolean isAuthRequest() {
		return url.matches(".+/rest/session$");
	}

	public boolean isPage() {
		return url.matches(".*/Provider\\?(\\w+=\\w+)(&\\w+=\\w+)*") || url.matches(".*/page/[\\w\\.]+");
	}

	public boolean isAnonymousPage() {
		return false;
	}

	public String getUrl() {
		return url;
	}

	public boolean isProtected() {
		return !appName.equals("") && !appID.equals("") || !(isDefault() || url.matches(".*/[\\w\\.-]+$") || appName.equalsIgnoreCase("SharedResources"));
	}

}
