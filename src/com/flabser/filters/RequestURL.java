package com.flabser.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {
	private String appName;
	private String appID;

	public RequestURL(String url) {
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)/([\\p{Lower}0-9]{16})?.*$");
		Matcher matcher = pattern.matcher(url != null ? url.trim() : "");
		if (matcher.matches()) {
			appName = matcher.group(1);
			appID = matcher.group(2);
		}
	}

	public String getAppName() {
		return appName;
	}

	public String getAppID() {
		return appID;
	}

}
