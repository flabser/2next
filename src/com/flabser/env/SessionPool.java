package com.flabser.env;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import com.flabser.script._Session;
import com.flabser.util.Util;

public class SessionPool {
	private static HashMap<Integer, _Session> userSessions = new HashMap<Integer, _Session>();

	public static String put(_Session us) {
		String sesID = Util.generateRandomAsText();
		int key = Base64.encodeBase64String(us.getCurrentUser().getLogin().getBytes(Charset.forName("UTF-8")))
				.hashCode();
		userSessions.put(key, us);
		String token = sesID + "#" + key;
		return token;
	}

	public static _Session getLoggeedUser(String token) {
		int key = 0;
		try {
			key = Integer.parseInt(token.substring(token.indexOf("#") + 1, token.length()));
		} catch (NumberFormatException e) {

		}
		_Session us = userSessions.get(key);
		if (us != null) {
			return us;
		} else {
			return null;
		}
	}

	public static void remove(_Session us) {
		userSessions.remove(us.getCurrentUser().getLogin());
	}

	public static HashMap<Integer, _Session> getUserSessions() {
		return userSessions;
	}

}
