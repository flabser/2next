package com.flabser.runtimeobj.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.flabser.script._Exception;
import com.flabser.script._IXMLContent;
import com.flabser.util.Util;
import com.flabser.util.XMLUtil;

public class Element  implements _IXMLContent {
	public String name = "";
	public String value;
	public HashMap<String, String> attrs = new HashMap<String, String>();

	public ArrayList<Element> tags =  new ArrayList<Element>();

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
		tags.add(tag);
		return tag;
	}
	
	public Element addTag(String tagName){
		Element tag = new Element(tagName, "");
		tags.add(tag);
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
		tags.add(tag);
		return tag;
	}
	
	public Element addTag(String tagName, Enum tagValue){
		Element tag = new Element(tagName, tagValue.toString());
		tags.add(tag);
		return tag;
	}

	public Element addTag(String tagName, Date tagValue){
		Element tag = new Element(tagName, Util.dateTimeFormat.format(tagValue));
		tags.add(tag);
		return tag;
	}

	public Element addTag(String tagName, String tagValue){
		Element tag = new Element(tagName, tagValue);
		tags.add(tag);
		return tag;
	}
	
	public Element addCDATATag(String tagName, String tagValue){
		Element tag = new Element(tagName, "<![CDATA[" + tagValue + "]]>");
		tags.add(tag);
		return tag;
	}
	
	
	
	public void setTagValue(String tagValue){
		value = tagValue;
	}

	public void setTagValue(int tagValue){
		value = Integer.toString(tagValue);
	}

	public void setAttr(String attrName, String attrValue){
		attrs.put(attrName, attrValue);			
	}

	public void setAttr(String attrName, Enum attrValue){
		attrs.put(attrName, attrValue.toString());			
	}
	
			
	public void setAttr(String attrName, boolean attrValue){
		attrs.put(attrName, Boolean.toString(attrValue));			
	}

	public void setAttr(String attrName, int attrValue){
		attrs.put(attrName, Integer.toString(attrValue));			
	}

	public StringBuffer toXML(){
		StringBuffer output = new StringBuffer(1000);
		for(Element tag: tags){
			output.append(tag.toXML());
		}
		String attrsContent = "";
		for(Entry<String, String> attr: attrs.entrySet()){
			attrsContent += " " + attr.getKey() + "=\"" + XMLUtil.getAsTagValue(attr.getValue()) + "\" ";
		}
		
		if (name.equalsIgnoreCase("")){
			return output;
		}else{
			
			return output.append("<" + name + attrsContent + ">" + XMLUtil.getAsTagValue(value) + output + "</" + name + ">");
		}		
	}
	
	
	
}