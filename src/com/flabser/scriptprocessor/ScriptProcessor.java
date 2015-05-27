package com.flabser.scriptprocessor;

import com.flabser.log.ILogger;
import com.flabser.log.Log4jLogger;
import groovy.lang.GroovyObject;

public class ScriptProcessor implements IScriptProcessor{
	public static ILogger logger = new Log4jLogger("ScriptProcessor");
	
	public String[] processString(String script) {
		logger.errorLogEntry("method 4563 has not reloaded");
		return null;
	}	

	@Override
	public String process(String script) {	
		logger.errorLogEntry("method 4564 has not reloaded");
		return "";
	}
	
	@Override
	public String[] processString(Class<GroovyObject> compiledClass) {
		logger.errorLogEntry("method 4565 has not reloaded");
		return null;
	}
	

	
	
	public String toString(){
		return "ScriptProcessorType=" + ScriptProcessorType.UNDEFINED;
	}

}
