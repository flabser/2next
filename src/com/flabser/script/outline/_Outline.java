package com.flabser.script.outline;

import java.util.ArrayList;
import com.flabser.rule.constants.RunMode;

public class _Outline {
	public RunMode isOn = RunMode.ON;
	public String caption = "";
	public String hint = "";
	public String customID;
		
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
	
}
