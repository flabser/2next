package com.flabser.script.outline;

import java.util.ArrayList;

import com.flabser.rule.constants.RunMode;
import com.flabser.util.XMLUtil;

public class _OutlineEntry {
	public RunMode isOn = RunMode.ON;
	public String caption;
	public String hint;
	public String url;
	public String customID;
    public int unread;
	
	private String value = "";
	private ArrayList<_OutlineEntry> entries = new ArrayList<_OutlineEntry>();
	
	_OutlineEntry(String caption, String hint, String customID, String url){
		this.caption = caption;
		this.hint = hint;
		this.url = url + "&entryid=" + caption.hashCode() + customID.hashCode() + "&title=" + caption;
		this.customID = customID;
	}
		
	public void setValue(String v){
		value = v;
	}
	
	public void setValue(int v){
		value = Integer.toString(v);
	}
	
	void addEntry(_OutlineEntry entry){
		entries.add(entry);
	}
	
	public String toXML() {
		String a = "";
		
		for(_OutlineEntry e: entries){
			a += e.toXML();
		}
		
		return "<entry mode=\"" + isOn +"\"" +  XMLUtil.getAsAttribute("url", url) + XMLUtil.getAsAttribute("id", customID) + XMLUtil.getAsAttribute("caption", caption) + XMLUtil.getAsAttribute("hint", hint) + XMLUtil.getAsAttribute("unread", String.valueOf(unread)) + ">" + XMLUtil.getAsTagValue(value) + a + "</entry>";
	}

}
