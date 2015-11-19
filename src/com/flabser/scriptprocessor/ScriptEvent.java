package com.flabser.scriptprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.flabser.localization.Vocabulary;
import com.flabser.script._Element;
import com.flabser.script._IContent;
import com.flabser.script._Session;
import com.flabser.script._URL;

public class ScriptEvent {
	protected Vocabulary vocabulary;
	protected ArrayList<_URL> redirects = new ArrayList<_URL>();
	protected ArrayList<_IContent> toPublishElement = new ArrayList<_IContent>();
	protected _Session ses;

	public void publishElement(String entryName, String value) {
		toPublishElement.add(new _Element(entryName, value));
	}

	public void publishElement(String entryName, int value) {
		toPublishElement.add(new _Element(entryName, value));
	}

	public void publishElement(String entryName, Collection<?> elements) {
		toPublishElement.add(new _Element(entryName, elements));
	}

	public void publishElement(String entryName, Map<?, ?> elements) {
		toPublishElement.add(new _Element(entryName, elements));
	}

	public void publishElement(String entryName, _IContent value) {
		toPublishElement.add(new _Element(entryName, value));
	}

	public void addRedirect(_URL url) {
		redirects.add(url);
	}

}
