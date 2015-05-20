package com.flabser.scriptprocessor.page;

import java.util.ArrayList;
import com.flabser.runtimeobj.page.Element;

public class PublishResult {
	public boolean continueOpen;
	public ArrayList<Element> toPublishElement = new ArrayList<Element>();
	public ArrayList<Element> toPublish = new ArrayList<Element>();
	
	PublishResult(boolean cs, ArrayList<Element> toPublish, ArrayList<Element> toPublishElement){
		continueOpen = cs;
		this.toPublish = toPublish;
		this.toPublishElement = toPublishElement;
	}
}
