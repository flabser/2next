package com.flabser.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestURL {
	String appName;
	String appID;

	RequestURL(String url) {
		Pattern pattern = Pattern.compile("^/(\\p{Alpha}+)/([\\p{Lower}0-9]{16}/)?[\\w\\.]+$");
		Matcher matcher = pattern.matcher(url != null ? url.trim() : "");
		if (matcher.matches()) {
			appName = matcher.group(1);
			appID = matcher.group(2);
			if(appID != null)
				appID = appID.substring(0, appID.length() - 1);
		}
	}

}
