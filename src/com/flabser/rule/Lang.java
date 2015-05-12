package com.flabser.rule;

import org.w3c.dom.Node;

import com.flabser.rule.constants.RunMode;
import com.flabser.util.XMLUtil;

public class Lang {
	public RunMode isOn = RunMode.ON;
	public boolean isValid = true;
	public String description;  
	public String id = "unknown";	
	public String name;
	public boolean isPrimary;

	Lang(Node node){
		id = XMLUtil.getTextContent(node,"@id", false);
		name = XMLUtil.getTextContent(node,".", false);
		
		if (XMLUtil.getTextContent(node,"@mode", false).equalsIgnoreCase("off")){                    
			isOn = RunMode.OFF;	
			isValid = false;
		}
		
		String ip = XMLUtil.getTextContent(node,"@isprimary", false);
		if (ip.equalsIgnoreCase("true") || ip.equals("1")){
			isPrimary = true;
		}
		
	}

	public String toString(){
		return "id:" + id + ", name:" + name;
	}
	
	public String toXML(){
		return "<ison>" + isOn + "</ison><id>" + id + "</id><name>" + name + "</name><isprimary>" + isPrimary + "</isprimary>";
	}
	
}
