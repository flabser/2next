package com.flabser.runtimeobj.page;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.rule.page.PageRule;
import com.flabser.script._Session;

public class IncludedPage extends Page {

	public IncludedPage(AppTemplate env, _Session ses, PageRule rule, String httpMethod, String context) {
		super(env, ses, rule, httpMethod, context);
	}

	@Override
	public String getID() {
		return "INCLUDED_PAGE_" + rule.id;
	}
}
