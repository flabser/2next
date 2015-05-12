package com.flabser.scriptprocessor;

import com.flabser.dataengine.IDatabase;
import com.flabser.script._Session;
import com.flabser.users.User;

import groovy.lang.GroovyObject;


public class SessionScriptProcessor extends ScriptProcessor{	
	private _Session session;

	public SessionScriptProcessor(IDatabase db, User user){
		super();		
		//session = new _Session(db.getParent(), user, this);
	}

	public String[] processString(String script) {
		try{
			IScriptSource myObject = setScriptLauncher(script, false);	
			myObject.setSession(session);
			return myObject.sessionProcess();
		}catch(Exception e){
			ScriptProcessor.logger.errorLogEntry(script);
			ScriptProcessor.logger.errorLogEntry(e);
			return null;
		}
	}

	public String[] processString(Class<GroovyObject> groovyClass) {
		try{
			IScriptSource myObject = (IScriptSource)groovyClass.newInstance();
			myObject.setSession(session);		
			return myObject.sessionProcess();			
		}catch(Exception e){		
			ScriptProcessor.logger.errorLogEntry(e);
			return null;
		}
	}
	
	public String toString(){
		return "type" + ScriptProcessorType.SESSION;
	}

}
