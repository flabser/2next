package com.flabser.script.actions;

import java.util.ArrayList;
import java.util.HashMap;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._IPOJOContent;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;


public class _ActionBar implements _IPOJOContent, _IXMLContent {
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
	
	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		output.append("<actionbar mode=\"" + isOn +"\" caption=\"" + caption + "\" hint=\"" +  hint + "\">");
		for(_Action act: actions){
			output.append(act.toXML());
		}
		return  output.append("</actionbar>");
	}
	
	public String toString(){
		return toXML().toString();
	}

	@Override
	public HashMap<String, Object> getJsonObject() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mode", isOn.name());
		map.put("caption", caption);
		map.put("hint", hint);
		for (_Action a: actions) {
			map.put(a.customID, a.getJsonObject());
		}
		return map;
	}
	
	
}
