package com.flabser.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.flabser.env.EnvConst;

public class SessionCooksValues {
	public String currentLang = "ENG";
	public String auth;

	public SessionCooksValues(HttpServletRequest request) {
		try {
			Cookie[] cooks = request.getCookies();
			if (cooks != null) {
				for (int i = 0; i < cooks.length; i++) {
					if (cooks[i].getName().equals(EnvConst.LANG_COOKIE_NAME)) {
						currentLang = cooks[i].getValue();
					} else if (cooks[i].getName().equals(EnvConst.AUTH_COOKIE_NAME)) {
						auth = cooks[i].getValue();
					}
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	public String toString() {
		return "lang=" + currentLang;
	}

}
