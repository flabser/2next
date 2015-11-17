package com.flabser.dataengine;

import org.eclipse.persistence.dynamic.DynamicHelper.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

public class Event extends SessionCustomizer {
	@Override
	public void customize(Session session) throws Exception {
		session.getEventManager().addListener(new SessionEventAdapter() {
			@Override
			public void postLogin(SessionEvent event) {
				System.out.println("event");
			}
		});
	}
}
