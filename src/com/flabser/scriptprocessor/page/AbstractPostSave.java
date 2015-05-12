package com.flabser.scriptprocessor.page;

import java.util.HashMap;

import com.flabser.appenv.AppEnv;
import com.flabser.exception.RuleException;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.scriptprocessor.ScriptEvent;


public abstract class AbstractPostSave extends ScriptEvent implements IPostSaveScript {	
	private _Session ses;
	private _Document doc;
	private AppEnv env;
	
	public void setSession(_Session ses){			
		this.ses = ses;
	}
		
	public void setDocument(_Document doc){
		this.doc = doc;
	}
	
	public void setUser(String user){
		
	}
	
	public void setAppEnv(AppEnv env){
		this.env = env;
	}
	
	public StringBuffer startPage(String id, HashMap<String, String[]> formData) throws RuleException, ClassNotFoundException{
		PageRule pageRule = (PageRule) env.ruleProvider.getRule("page", id);	
		Page page = new Page(env, ses.getUser().getSession() , pageRule);		
		return page.process(formData);
	}
	
	public void process(){
		try{
			doPostSave(ses, doc);		
		}catch(Exception e){
			AppEnv.logger.errorLogEntry(e);
		}	
	}
		
	public abstract void doPostSave(_Session ses, _Document doc);
	
	
}
