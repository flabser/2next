package com.flabser.scriptprocessor.page;

import com.flabser.users.UserSession;

public class DoAsyncProcessor {
	private UserSession userSession;
	
	public DoAsyncProcessor(UserSession userSession) {
		this.userSession = userSession;
	}

	public String processScript(String id) throws ClassNotFoundException {		
		IAsyncScript asyncInstance = (IAsyncScript)userSession.getDynmaicClass(id);
		return asyncInstance.process();
		
	}

	
}
