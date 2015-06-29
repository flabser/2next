package com.flabser.env;

import com.flabser.users.UserSession;

import java.util.HashMap;

public class SessionPool {
    private static HashMap<String, UserSession> userSessions = new HashMap <String, UserSession>();

    public static void put(UserSession us){
        userSessions.put(us.currentUser.getLogin(),us);
    }

}
