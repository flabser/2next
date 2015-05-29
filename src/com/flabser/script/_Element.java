package com.flabser.script;

import java.util.Collection;
import java.util.Date;
import com.flabser.runtimeobj.page.Element;
import com.flabser.util.Util;

public class _Element implements _IContent {

	private Element element;

	public Element getElement() {
		return element;
	}

	public _Element() {
		element = new Element("", "");
	}

	public _Element(String name) {
		element = new Element(name, "");
	}

	public _Element(String name, String value) {
		element = new Element(name, value);
	}

	
	public _Element(String name, int value) {
		element = new Element(name, value);
	}

	public _Element(String name, Collection <_Element> elements) {
		element = new Element(name, "");
		for (_Element t : elements) {
			addElement(t);
		}
	}

	_Element(Element tag) {
		element = tag;
	}	

	public void addElement(_Element e) {
		element.addElement(e.element);
	
	}

	public _Element addElement(String name) {
		element.addElement(new Element(name, ""));
		return new _Element(element);
	}	

	public _Element addElement(String name, int value) {
		element.addElement(new Element(name, value));
		return new _Element(element);
	}

	public _Element addElement(String tagName, Date value) {
		element.addElement(new Element(tagName, Util.dateTimeFormat.format(value)));
		return new _Element(element);
	}

	public void addElement(String tagName, Collection <_Element> tagsList) {
		for (_Element t : tagsList) {
			element.addElement(t.element);
		}
	}

	public _Element addElement(String name, String value) {
		element.addElement(new Element(name, value));
		return new _Element(element);
	}


	public void setValue(String value) {
		element.setValue(value);
	}

	public void setValue(int value) {
		element.setValue(Integer.toString(value));
	}

	@Override
	public StringBuffer toXML() throws _Exception {
		StringBuffer output = new StringBuffer(1000);
		
		if (!element.getName().equalsIgnoreCase(""))	output.append("<" + element.getName() + ">");
		
		if (element.getValue() instanceof _IContent) {
			output.append(((_IContent)element.getValue()).toXML());
		}else {
			output.append(element.getValue());
		}
		
		for (Element e : element.getElements()) {
			if (e.getValue() instanceof _IContent) {
				output.append(((_IContent)e.getValue()).toXML());
			}else {
				output.append(e.toPublishAsXML());
			}
			
		}
		if (!element.getName().equalsIgnoreCase(""))	output.append("</" + element.getName() + ">");
		
		return output;			
			
	}	

}
