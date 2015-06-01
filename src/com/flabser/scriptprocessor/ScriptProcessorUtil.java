package com.flabser.scriptprocessor;

import com.flabser.script._Element;


public class ScriptProcessorUtil {
	

	
	public static _Element getScriptError(StackTraceElement stack[]){	
		_Element tag = new _Element("stack","");
		for (int i=0; i<stack.length; i++){
			//tag.addElement(new _Element("entry", stack[i].getClassName()+"(" + stack[i].getMethodName() + ":" + Integer.toString(stack[i].getLineNumber()) + ")"));			
		}
		return tag;
	}
	
	public static String getGroovyError(StackTraceElement stack[]){		
		for (int i=0; i<stack.length; i++){
			if (stack[i].getClassName().contains("Foo")){
				return stack[i].getMethodName()+", > "+Integer.toString(stack[i].getLineNumber() - 3) + "\n";	
			}
		}
		return "";
	}
	
}
