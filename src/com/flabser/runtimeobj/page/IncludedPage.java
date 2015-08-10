package com.flabser.runtimeobj.page;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.rule.page.PageRule;
import com.flabser.users.UserSession;


public class IncludedPage extends Page {

	public IncludedPage(AppTemplate env, UserSession userSession, PageRule rule, String httpMethod) {
		super(env, userSession, rule, httpMethod);
	}

	public String getID() {
		return "INCLUDED_PAGE_" + rule.id;
	}
}
