package com.flabser.runtimeobj.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.flabser.util.Util;
import com.flabser.util.XMLUtil;

public class Tag{
	public String name;
	public String value;
	public HashMap<String, String> attrs = new HashMap<String, String>();

	public ArrayList<Tag> tags =  new ArrayList<Tag>();

	public Tag(String tagName, String tagValue){
		name = tagName;
		value = tagValue;
	}

	public Tag(String tagName, int tagValue){
		name = tagName;
		value = Integer.toString(tagValue);
	}
	
	public Tag(String tagName){
		name = tagName;
		value = "";
	}

	public Tag(String tagName, BigDecimal tagValue) {
		name = tagName;
		value = tagValue.toString();
	}

	public Tag addTag(Tag tag){		
		tags.add(tag);
		return tag;
	}
	
	public Tag addTag(String tagName){
		Tag tag = new Tag(tagName, "");
		tags.add(tag);
		return tag;
	}


	public Tag addTag(String tagName, int tagValue){
		Tag tag = new Tag(tagName, tagValue);
		tags.add(tag);
		return tag;
	}
	
	public Tag addTag(String tagName, Enum tagValue){
		Tag tag = new Tag(tagName, tagValue.toString());
		tags.add(tag);
		return tag;
	}

	public Tag addTag(String tagName, Date tagValue){
		Tag tag = new Tag(tagName, Util.dateTimeFormat.format(tagValue));
		tags.add(tag);
		return tag;
	}

	public Tag addTag(String tagName, String tagValue){
		Tag tag = new Tag(tagName, tagValue);
		tags.add(tag);
		return tag;
	}
	
	public Tag addCDATATag(String tagName, String tagValue){
		Tag tag = new Tag(tagName, "<![CDATA[" + tagValue + "]]>");
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

	public String toXML(){
		String nestedContent = "";
		for(Tag tag: tags){
			nestedContent += tag.toXML();
		}
		String attrsContent = "";
		for(Entry<String, String> attr: attrs.entrySet()){
			attrsContent += " " + attr.getKey() + "=\"" + XMLUtil.getAsTagValue(attr.getValue()) + "\" ";
		}
		
		if (name.equalsIgnoreCase("")){
			return nestedContent;
		}else{
			
			return "<" + name + attrsContent + ">" + XMLUtil.getAsTagValue(value) + nestedContent + "</" + name + ">";
		}		
	}
	
	
	
}