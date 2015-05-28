package com.flabser.script;

import java.util.Collection;
import java.util.Date;
import com.flabser.runtimeobj.page.Element;
import com.flabser.util.Util;

public class _Element {

	private Element element;

	public _Element() {
		element = new Element("", "");
	}

	public _Element(String name) {
		element = new Element(name, "");
	}

	public _Element(String name, String value) {
		element = new Element(name, value);
	}

	public _Element(String name, Object value) {
		element = new Element(name, value.toString());
	}

	public _Element(String name, int value) {
		element = new Element(name, value);
	}

	public _Element(String name, Collection <_Element> elements) {
		element = new Element(name, "");
		for (_Element t : elements) {
			addElements(t);
		}
	}

	_Element(Element tag) {
		element = tag;
	}	

	public _Element addElements(_Element tag) {
		element.elements.add(tag.element);
		return tag;
	}

	public _Element addElement(String name) {
		Element element = new Element(name, "");
		element.elements.add(element);
		return new _Element(element);
	}	

	public _Element addElement(String name, int value) {
		Element element = new Element(name, value);
		element.elements.add(element);
		return new _Element(element);
	}

	public _Element addElement(String tagName, Date tagValue) {
		Element element = new Element(tagName, Util.dateTimeFormat.format(tagValue));
		element.elements.add(element);
		return new _Element(element);
	}

	public void addElement(String tagName, Collection <_Element> tagsList) {
		for (_Element t : tagsList) {
			element.elements.add(t.element);
		}
	}

	public _Element addElement(String name, String value) {
		Element element = new Element(name, value);
		element.elements.add(element);
		return new _Element(element);
	}


	public void setValue(String tagValue) {
		element.value = tagValue;
	}

	public void setValue(int tagValue) {
		element.value = Integer.toString(tagValue);
	}	

}
