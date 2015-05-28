package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import com.flabser.script._Exception;
import com.flabser.script._IContent;

public class Element{
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
		
	public Element addTag(Element tag){		
		elements.add(tag);
		return tag;
	}
	
	
	public Element(String entryName, _IContent object) {
		name = entryName;
		value = object;
	}
	
	public StringBuffer toXML() throws _Exception{
		StringBuffer output = new StringBuffer(1000);
		
		if (!name.equalsIgnoreCase(""))	output.append("<" + name + ">");
		
		if (value instanceof _IContent) {
			output.append(((_IContent)value).toXML());
		}else {
			output.append(value);
		}
		
		for (Element e : elements) {
			if (e.value instanceof _IContent) {
				output.append(((_IContent)e.value).toXML());
			}else {
				output.append(e.toXML());
			}
			
		}
		if (!name.equalsIgnoreCase(""))	output.append("</" + name + ">");
		
		return output;			
			
				
	}
		
}