package com.flabser.scriptprocessor;

import com.flabser.script._Session;

import groovy.lang.GroovyObject;

public class SimpleScriptProcessor extends ScriptProcessor{
	private _Session session;
	
	public SimpleScriptProcessor(){
		super();
	}

	public SimpleScriptProcessor(_Session ses) {
		super();
		session = ses;
	}
	
	
	public String[] processString(String script){
		try{
			IScriptSource myObject = setScriptLauncher(script, false);			
			myObject.setSession(this.session);			
			return myObject.sessionProcess();
		}catch(Exception e){
			ScriptProcessor.logger.errorLogEntry(script);
			ScriptProcessor.logger.errorLogEntry(e);
			return null;
		}
	}
	
	public String[] processString(Class<GroovyObject> groovyClass){
		try{
			IScriptSource myObject = (IScriptSource) groovyClass.newInstance();	
			myObject.setSession(this.session);				
			return myObject.sessionProcess();
		}catch(Exception e){
			ScriptProcessor.logger.errorLogEntry(e);
			return null;
		}
	}

	public String toString(){
		return "type=" + ScriptProcessorType.SIMPLE.toString();
	}

}
