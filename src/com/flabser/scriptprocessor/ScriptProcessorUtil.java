package com.flabser.scriptprocessor;

import com.flabser.runtimeobj.page.Element;


public class ScriptProcessorUtil {
	

	
	public static Element getScriptError(StackTraceElement stack[]){	
		Element tag = new Element("stack","");
		for (int i=0; i<stack.length; i++){
			tag.addTag(new Element("entry", stack[i].getClassName()+"(" + stack[i].getMethodName() + ":" + Integer.toString(stack[i].getLineNumber()) + ")"));			
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
