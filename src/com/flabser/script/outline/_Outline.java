package com.flabser.script.outline;

import java.util.ArrayList;

import com.flabser.rule.constants.RunMode;
import com.flabser.script._IContent;

public class _Outline implements _IContent {
	private RunMode isOn = RunMode.ON;
	private String caption = "";
	private String hint = "";
	private String customID;		
	private ArrayList<_Outline> outlines = new ArrayList<_Outline>();
	private ArrayList<_OutlineEntry> entries = new ArrayList<_OutlineEntry>();
	
	_Outline(String caption, String hint, String customID){
		this.caption = caption;
		this.hint = hint;		
		this.customID = customID;
	}
	
	void addOutline(_Outline outl){
		outlines.add(outl);
	}
	
	void addEntry(_OutlineEntry entry){
		entries.add(entry);
	}
	
	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		
		for(_Outline o: outlines){
			output.append(o.toXML());
		}
		
		for(_OutlineEntry e: entries){
			output.append(e.toXML());
		}
		
		return output.append("<outline mode=\"" + isOn +"\" id=\"" + customID + "\" caption=\"" + caption + "\" hint=\"" +  hint + "\">" + output + "</outline>");
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

	public ArrayList<_OutlineEntry> getEntries() {
		return entries;
	}
	
}
