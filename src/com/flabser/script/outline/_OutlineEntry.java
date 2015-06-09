package com.flabser.script.outline;

import com.flabser.util.XMLUtil;

public class _OutlineEntry extends _Outline {
	public String url;
    public int unread;	
		
	_OutlineEntry(String caption, String hint, String customID, String url){
		super(caption, hint, customID);
		this.caption = caption;
		this.hint = hint;
		this.url = url + "&entryid=" + caption.hashCode() + customID.hashCode() + "&title=" + caption;
		this.customID = customID;
	}		
	
	
	public StringBuffer toXML() {
		StringBuffer output = new StringBuffer(1000);
		return output.append("<entry mode=\"" + isOn +"\"" +  XMLUtil.getAsAttribute("url", url) + XMLUtil.getAsAttribute("id", customID) + 
				XMLUtil.getAsAttribute("caption", caption) + XMLUtil.getAsAttribute("hint", hint) + XMLUtil.getAsAttribute("unread", String.valueOf(unread)) + "/>");
	}


	public String getUrl() {
		return url;
	}


	public int getUnread() {
		return unread;
	}

}
