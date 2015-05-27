package com.flabser.script.actions;

import java.util.ArrayList;
import java.util.HashMap;

import com.flabser.rule.constants.RunMode;
import com.flabser.runtimeobj.page.Element;
import com.flabser.script.IPOJOContent;
import com.flabser.script._Session;


public class _ActionBar implements IPOJOContent {
	public RunMode isOn = RunMode.ON;
	public String caption = "";
	public String hint = "";
		
	private ArrayList<_Action> actions = new ArrayList<_Action>();
	private _Session session;
		
	public _ActionBar(_Session ses){
		session = ses;
	}
	
	void addAction(_Action action){
		action.setSession(session);
		actions.add(action);
	}
	
	public String toXML(){
		String a = "";
		for(_Action act: actions){
			a += act.toXML();
		}
		return "<actionbar mode=\"" + isOn +"\" caption=\"" + caption + "\" hint=\"" +  hint + "\">" + a + "</actionbar>";
	}
	
	public String toString(){
		return toXML();
	}

	@Override
	public HashMap<String, String> getObject() {
		HashMap<String, String> elementsMap = new HashMap<String, String>();
		elementsMap.put("mode", isOn.name());
		elementsMap.put("caption", caption);
		elementsMap.put("hint", hint);
		elementsMap.put("action", isOn.name());
		return elementsMap;
	}
	
	
}
