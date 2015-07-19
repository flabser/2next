package com.flabser.env;

import java.util.HashMap;

import com.flabser.users.UserSession;

public class SessionPool {
	private static HashMap<String, UserSession> userSessions = new HashMap<String, UserSession>();

	public static void put(UserSession us) {
		userSessions.put(us.currentUser.getLogin(), us);
	}

	public static void remove(UserSession us) {
		userSessions.remove(us.currentUser.getLogin());
	}

}
