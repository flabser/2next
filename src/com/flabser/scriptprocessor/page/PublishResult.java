package com.flabser.scriptprocessor.page;

import java.util.ArrayList;
import com.flabser.script._IContent;

public class PublishResult {
	public boolean continueOpen;
	public ArrayList<_IContent> toPublishElement = new ArrayList<_IContent>();
	
	PublishResult(boolean cs, ArrayList<_IContent> toPublish, ArrayList<_IContent> toPublishElement){
		continueOpen = cs;
		this.toPublishElement = toPublishElement;
	}
}
