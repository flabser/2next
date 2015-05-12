package com.flabser.script.events;

import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.scriptprocessor.page.AbstractPostSave;


public abstract class _FormPostSave extends AbstractPostSave{
	public abstract void doPostSave(_Session ses, _Document doc);
	
}
