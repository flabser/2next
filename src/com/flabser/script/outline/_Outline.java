package com.flabser.script.outline;

import java.util.ArrayList;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._IContent;

public class _Outline implements _IContent {
	protected RunMode isOn = RunMode.ON;
	protected String caption = "";
	protected String hint = "";
	protected String customID;		
	private ArrayList<_Outline> outlines = new ArrayList<_Outline>();
	
	_Outline(String caption, String hint, String customID){
		this.caption = caption;
		this.hint = hint;		
		this.customID = customID;
	}
	
	void addEntry(_Outline outl){
		outlines.add(outl);
	}	
	
	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		StringBuffer nestedOutput = new StringBuffer(500);
		
		for(_Outline o: outlines){
			nestedOutput.append(o.toXML());
			
		}		
		output.append("<outline mode=\"" + isOn +"\" id=\"" + customID + "\" caption=\"" + caption + "\" hint=\"" +  hint + "\">" + nestedOutput + "</outline>");
		return output; 
	}

	public RunMode getIsOn() {
		return isOn;
	}

	public String getCaption() {
		return caption;
	}

	public String getHint() {
		return hint;
	}

	public String getCustomID() {
		return customID;
	}

	public ArrayList<_Outline> getOutlines() {
		return outlines;
	}

	public ArrayList<_Outline> getEntries() {
		return outlines;
	}
	
}
