package com.flabser.script;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import com.flabser.runtimeobj.page.Element;
import com.flabser.util.Util;

public class _Element {

	private Element runtimeTag;

	public _Element() {
		runtimeTag = new Element("", "");
	}

	public _Element(String tagName) {
		runtimeTag = new Element(tagName, "");
	}

	public _Element(String tagName, String tagValue) {
		runtimeTag = new Element(tagName, tagValue);
	}

	public _Element(String tagName, Object tagValue) {
		runtimeTag = new Element(tagName, tagValue.toString());
	}

	public _Element(String tagName, int tagValue) {
		runtimeTag = new Element(tagName, tagValue);
	}

	public _Element(String tagName, Collection <_Element> tags) {
		runtimeTag = new Element(tagName, "");
		for (_Element t : tags) {
			addTag(t);
		}
	}

	_Element(Element tag) {
		runtimeTag = tag;
	}	

	public _Element addTag(_Element tag) {
		runtimeTag.elements.add(tag.runtimeTag);
		return tag;
	}

	public _Element addTag(String tagName) {
		Element tag = new Element(tagName, "");
		runtimeTag.elements.add(tag);
		return new _Element(tag);
	}	

	public _Element addTag(String tagName, int tagValue) {
		Element tag = new Element(tagName, tagValue);
		runtimeTag.elements.add(tag);
		return new _Element(tag);
	}

	public _Element addTag(String tagName, Date tagValue) {
		Element tag = new Element(tagName, Util.dateTimeFormat.format(tagValue));
		runtimeTag.elements.add(tag);
		return new _Element(tag);
	}

	public void addTag(String tagName, Collection <_Element> tagsList) {
		for (_Element t : tagsList) {
			runtimeTag.elements.add(t.runtimeTag);
		}
	}

	public _Element addTag(String tagName, String tagValue) {
		Element tag = new Element(tagName, tagValue);
		runtimeTag.elements.add(tag);
		return new _Element(tag);
	}

	public _Element addCDATATag(String tagName, String tagValue) {
		Element tag = new Element(tagName, "<![CDATA[" + tagValue + "]]>");
		runtimeTag.elements.add(tag);
		return new _Element(tag);
	}



	public void setTagValue(String tagValue) {
		runtimeTag.value = tagValue;
	}

	public void setTagValue(int tagValue) {
		runtimeTag.value = Integer.toString(tagValue);
	}	

}
