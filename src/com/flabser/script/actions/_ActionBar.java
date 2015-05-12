package com.flabser.script.actions;

import java.util.ArrayList;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._Exception;
import com.flabser.script._ExceptionType;
import com.flabser.script._Session;


public class _ActionBar {
	public RunMode isOn = RunMode.ON;
	public String caption = "";
	public String hint = "";
		
	private ArrayList<_Action> actions = new ArrayList<_Action>();
	private _Session session;
	
	public _ActionBar() throws _Exception{
		throw new _Exception(_ExceptionType.CONSTRUCTOR_UNDEFINED, "Defaul constructor undefined, you should use  \"new _ActionBar(_Session)\"");
	}
	
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
	
	
}
