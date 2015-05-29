package com.flabser.runtimeobj.page;

import java.util.ArrayList;
import com.flabser.script._Exception;
import com.flabser.script._IContent;

public class Element{
	private String name = "";
	private Object value;
	private ArrayList<Element> elements =  new ArrayList<Element>();

	public Element(String name, String value){
		this.name = name;
		this.value = value;
	}

	public Element(String name, int value){
		this.name = name;
		this.value = Integer.toString(value);
	}
		
	public void addElement(Element tag){		
		elements.add(tag);
	}	
	
	public Element(String entryName, _IContent object) {
		name = entryName;
		value = object;
	}
	
	public StringBuffer toPublishAsXML() throws _Exception{
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
				output.append(e.toPublishAsXML());
			}
			
		}
		if (!name.equalsIgnoreCase(""))	output.append("</" + name + ">");
		
		return output;			
			
				
	}

	public void setValue(String value) {
		this.value = value;		
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}
		
}