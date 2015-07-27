package com.flabser.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Cookies {
	public String currentLang = "ENG";
	public String auth;

	public Cookies(HttpServletRequest request) {
		Cookie[] cooks = request.getCookies();
		if (cooks != null) {
			for (int i = 0; i < cooks.length; i++) {
				if (cooks[i].getName().equals("lang")) {
					currentLang = cooks[i].getValue();
				} else if (cooks[i].getName().equals("2nses")) {
					auth = cooks[i].getValue();
				}
			}
		}
	}

	@Override
	public String toString() {
		return "lang=" + currentLang;
	}

}
