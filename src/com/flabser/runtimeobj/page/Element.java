package com.flabser.runtimeobj.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.flabser.script._Exception;
import com.flabser.script._IXMLContent;
import com.flabser.util.Util;
import com.flabser.util.XMLUtil;

public class Element  implements _IXMLContent {
	public String name = "";
	public String value;
	public ArrayList<Element> elements =  new ArrayList<Element>();

	public Element(String tagName, String tagValue){
		name = tagName;
		value = tagValue;
	}

	public Element(String tagName, int tagValue){
		name = tagName;
		value = Integer.toString(tagValue);
	}
	
	public Element(String tagName){
		name = tagName;
		value = "";
	}

	public Element(String tagName, BigDecimal tagValue) {
		name = tagName;
		value = tagValue.toString();
	}

	public Element addTag(Element tag){		
		elements.add(tag);
		return tag;
	}
	
	public Element addTag(String tagName){
		Element tag = new Element(tagName, "");
		elements.add(tag);
		return tag;
	}

	public Element(String name, _IXMLContent value) throws _Exception{
		this.name = name;
		this.value = value.toXML().toString();
	
	}
	public Element(String entryName, String result, boolean b) {
		// TODO Auto-generated constructor stub
	}

	public Element(String entryName, int idValue, String value2) {
		// TODO Auto-generated constructor stub
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
			if (elements.size() > 0 && value.equalsIgnoreCase("")) {
				return output.append("<" + name + "><elements>"  + output + "</elements></" + name + ">");
			}else {
				return output.append("<" + name + ">" + XMLUtil.getAsTagValue(value) + "<elements>" + output + "</elements></" + name + ">");
			}
		}		
	}
	
	
	
}