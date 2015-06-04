package com.flabser.runtimeobj.page;

import com.flabser.appenv.AppEnv;
import com.flabser.rule.page.PageRule;
import com.flabser.users.UserSession;


public class IncludedPage extends Page{
	
	public IncludedPage(AppEnv env, UserSession userSession, PageRule rule){
		super(env,userSession,rule);
	}
	
	public String getID(){
		return "INCLUDED_PAGE_" + rule.id + "_" + userSession.lang;
		
	}
	
	
}
