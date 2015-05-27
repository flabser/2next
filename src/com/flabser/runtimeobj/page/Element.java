package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.flabser.script._IXMLContent;
import com.flabser.util.Util;
import com.flabser.util.XMLUtil;

public class Element  implements _IXMLContent {
	public String name = "";
	public Object value;
	public ArrayList<Element> elements =  new ArrayList<Element>();

	public Element(String tagName, String tagValue){
		name = tagName;
		value = tagValue;
	}

	public Element(String tagName, int tagValue){
		name = tagName;
		value = Integer.toString(tagValue);
	}
	
	public Element(Object o){
		value = o;		
	}
	
	public Element addTag(Element tag){		
		elements.add(tag);
		return tag;
	}
	
	

	public Element(String tagName, Element scriptError) {
		// TODO Auto-generated constructor stub
	}

	public Element(String entryName, HashMap<String, String> object) {
		name = entryName;
		value = object;
	}

	public Element addTag(String tagName, int tagValue){
		Element tag = new Element(tagName, tagValue);
		elements.add(tag);
		return tag;
	}
	
	public Element addTag(String tagName, Enum tagValue){
		Element tag = new Element(tagName, tagValue.toString());
		elements.add(tag);
		return tag;
	}

	public Element addTag(String tagName, Date tagValue){
		Element tag = new Element(tagName, Util.dateTimeFormat.format(tagValue));
		elements.add(tag);
		return tag;
	}

	public Element addTag(String tagName, String tagValue){
		Element tag = new Element(tagName, tagValue);
		elements.add(tag);
		return tag;
	}
	
	public Element addCDATATag(String tagName, String tagValue){
		Element tag = new Element(tagName, "<![CDATA[" + tagValue + "]]>");
		elements.add(tag);
		return tag;
	}
	
	
	
	public void setTagValue(String tagValue){
		value = tagValue;
	}

	public void setTagValue(int tagValue){
		value = Integer.toString(tagValue);
	}
	

	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		for(Element tag: elements){
			output.append(tag.toXML());
		}		
		
		if (name.equalsIgnoreCase("")){
			return output;
		}else{
			if (elements.size() > 0 && value.toString().equalsIgnoreCase("")) {
				return output.append("<" + name + "><elements>"  + output + "</elements></" + name + ">");
			}else {
				return output.append("<" + name + ">" + XMLUtil.getAsTagValue(value.toString()) + "<elements>" + output + "</elements></" + name + ">");
			}
		}		
	}
	
	
	
}