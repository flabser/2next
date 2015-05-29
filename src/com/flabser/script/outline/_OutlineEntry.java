package com.flabser.script.outline;

import com.flabser.rule.constants.RunMode;
import com.flabser.util.XMLUtil;

public class _OutlineEntry {
	public RunMode isOn = RunMode.ON;
	public String caption;
	public String hint;
	public String url;
	public String customID;
    public int unread;	
		
	_OutlineEntry(String caption, String hint, String customID, String url){
		this.caption = caption;
		this.hint = hint;
		this.url = url + "&entryid=" + caption.hashCode() + customID.hashCode() + "&title=" + caption;
		this.customID = customID;
	}		
	
	
	public String toXML() {
		return "<entry mode=\"" + isOn +"\"" +  XMLUtil.getAsAttribute("url", url) + XMLUtil.getAsAttribute("id", customID) + XMLUtil.getAsAttribute("caption", caption) + XMLUtil.getAsAttribute("hint", hint) + XMLUtil.getAsAttribute("unread", String.valueOf(unread)) + "/>";
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


	public String getUrl() {
		return url;
	}


	public String getCustomID() {
		return customID;
	}


	public int getUnread() {
		return unread;
	}

}
