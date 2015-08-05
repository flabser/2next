package com.flabser.env;

import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import com.flabser.users.UserSession;
import com.flabser.util.Util;

public class SessionPool {
	private static HashMap<Integer, UserSession> userSessions = new HashMap<Integer, UserSession>();

	public static String put(UserSession us) {
		String sesID = Util.generateRandomAsText();
		int key = Base64.encodeBase64String(us.currentUser.getLogin().getBytes(Charset.forName("UTF-8"))).hashCode();
		userSessions.put(key, us);
		return sesID + "#" + key;
	}

	public static UserSession getLoggeedUser(String token) {
		int key = 0;
		try {
			key = Integer.parseInt(token.substring(token.indexOf("#") + 1, token.length()));
		} catch (NumberFormatException e) {

		}
		UserSession us = userSessions.get(key);
		return us;
	}

	public static void remove(UserSession us) {
		userSessions.remove(us.currentUser.getLogin());
	}

	public static HashMap<Integer, UserSession> getUserSessions() {
		return userSessions;
	}

}
