package com.flabser.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.flabser.rule.page.CachingStrategyType;
import com.flabser.runtimeobj.page.Page;

public class _Page {
	private String id;
	private CachingStrategyType caching;
	private String elapsed_time;
	private ArrayList<_Page> includedPages = new ArrayList<_Page>();
	private HashMap<String, _IContent> elementsMap = new HashMap<String, _IContent>();
	private ArrayList<_IContent> elementsList = new ArrayList<_IContent>();
	private HashMap<String, String[]> captions;

	public _Page(Page page, _WebFormData webFormData) {

	}

	public _Page() {

	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCaching(CachingStrategyType caching) {
		this.caching = caching;
	}

	public void addPage(_Page page) {
		includedPages.add(page);
	}

	public void addElement(String key, _IContent element) {
		elementsMap.put(key, element);
		elementsList.add(element);
	}

	public void addElements(ArrayList<_IContent> elements) {
		elementsList.addAll(elements);
	}

	public ArrayList<_Page> getIncludedPages() {
		return includedPages;
	}

	public ArrayList<_IContent> getElements() {
		return elementsList;
	}

	public void setCaptions(HashMap<String, String[]> captions) {
		this.captions = captions;
	}

	public HashMap<String, String[]> getCaptions() {
		return captions;
	}

	public StringBuffer toXML() throws _Exception {
		StringBuffer output = new StringBuffer(5000);
		output.append("<page id=\"" + id + "\" cache=\"" + caching + "\" elapsed_time = \"" + elapsed_time + "\" >");

		for (_IContent e : elementsList) {
			output.append(e.toXML());
		}

		for (_Page p : includedPages) {
			output.append(p.toXML());
		}

		StringBuffer captionsText = new StringBuffer("<captions>");
		for (Entry<String, String[]> entry : captions.entrySet()) {
			captionsText.append("<" + entry.getKey() + " caption=\"" + entry.getValue()[0] + "\" />");
		}

		captionsText.append("</captions>");

		return output.append(captionsText).append("</page>");
	}

	public String getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(String string) {
		this.elapsed_time = string;
	}

}
