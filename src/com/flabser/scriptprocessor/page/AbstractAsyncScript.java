package com.flabser.scriptprocessor.page;

import java.util.ArrayList;


public abstract class AbstractAsyncScript implements IAsyncScript {

	protected ArrayList <String> toPublishElement = new ArrayList <String>();

	public void publishJSON(String json) {
		toPublishElement.add(json);
	}

	public void clearPublish() {
		toPublishElement.clear();
	}

	@Override
	public String process() {
		String val = "";
		doProcess();
		for (String v : toPublishElement) {
			val += v;
		}
		return val;
	}

	public abstract void doProcess();
}
