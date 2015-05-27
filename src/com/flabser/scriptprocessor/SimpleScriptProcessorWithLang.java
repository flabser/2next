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
	

	public String toString(){
		return "type=" + ScriptProcessorType.SIMPLE_WITH_LANG.toString();
	}
}
