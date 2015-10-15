package com.flabser.runtimeobj.page;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.rule.page.PageRule;
import com.flabser.users.UserSession;


public class IncludedPage extends Page {

	public IncludedPage(AppTemplate env, UserSession userSession, PageRule rule, String httpMethod, String context) {
		super(env, userSession, rule, httpMethod, context);
	}

	@Override
	public String getID() {
		return "INCLUDED_PAGE_" + rule.id;
	}
}
