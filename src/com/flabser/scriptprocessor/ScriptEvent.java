package com.flabser.scriptprocessor;

import java.util.ArrayList;

import com.flabser.env.Environment;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Element;
import com.flabser.script._IContent;
import com.flabser.script._Session;
import com.flabser.script._URL;

public class ScriptEvent {
	protected Vocabulary vocabulary;
	protected String redirectURL = "";
	protected ArrayList<_IContent> toPublishElement = new ArrayList<_IContent>();
	protected _Session ses;
	
	
	public void publishElement(String entryName, String value){
		toPublishElement.add(new  _Element(entryName, value));
	}
	
	public void publishElement(String entryName, int value){
		toPublishElement.add(new  _Element(entryName, value));
	}	
	
	public void publishElement(String entryName, ArrayList<String[]> elements){
		toPublishElement.add(new  _Element(entryName, elements));
	}	
	
	public void publishElement(String entryName, _IContent value){
		toPublishElement.add(new _Element(entryName, value)); 
	}
		
	public  String getGroovyError(StackTraceElement stack[]){		
		for (int i=0; i<stack.length; i++){
			if (stack[i].getClassName().contains(this.getClass().getName())){
				return stack[i].getClassName() + " method=" + stack[i].getMethodName() + " > "+Integer.toString(stack[i].getLineNumber()) + "\n";	
			}
		}
		return "";
	}

	public String getTmpDirPath(){
		return Environment.tmpDir;	
	}

	
	
	public String getWord(String word, Vocabulary vocabulary, String lang){
		try{
			return vocabulary.getSentenceCaption(word, lang).word;
		}catch(Exception e){
			return word.toString();
		}
	}
	
	public String getLocalizedWord(String word, String lang){
		return getWord(word, vocabulary, lang);
	}

	public void setRedirectURL(_URL url){
		redirectURL = url.toString();
	}

	public static void log(Object logText){
		ScriptProcessor.logger.normalLogEntry(logText.toString());
	}
	
}
