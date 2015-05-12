package com.flabser.scriptprocessor;

import com.flabser.script._Session;



public class SimpleScriptProcessorWithLang extends ScriptProcessor{
	private _Session session;
	private String lang;
	
	public SimpleScriptProcessorWithLang(_Session ses, String lang) {
		super();
		this.lang = lang;
		session = ses;
	}
	
	
	public String[] processString(String script){
		try{
			IScriptSource myObject = setScriptLauncher(script, false);			
			myObject.setSession(this.session);
			myObject.setLang(lang);
			return myObject.sessionLangProcess();
		}catch(Exception e){
			ScriptProcessor.logger.errorLogEntry(script);
			ScriptProcessor.logger.errorLogEntry(e);
			return null;
		}
	}

	public String toString(){
		return "type=" + ScriptProcessorType.SIMPLE_WITH_LANG.toString();
	}
}
