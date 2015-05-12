package com.flabser.script.events;

import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.page.AbstractQuerySave;


public abstract class _FormQuerySave extends AbstractQuerySave{
	public abstract void doQuerySave(_Session ses, _Document doc,_WebFormData webFormData, String lang);
	
}
