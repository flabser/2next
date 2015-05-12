package com.flabser.scriptprocessor;

import groovy.lang.GroovyObject;

public interface IScriptProcessor {	
	String[] processString(String script);	
	String process(String script);
	String[] processString(Class<GroovyObject> compiledClass);
	
}
