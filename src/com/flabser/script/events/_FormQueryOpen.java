package com.flabser.script.events;

import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.page.AbstractQueryOpen;


public abstract class _FormQueryOpen extends AbstractQueryOpen{
	
	public abstract void doQueryOpen(_Session session, _WebFormData webFormData, String lang);
	public abstract void doQueryOpen(_Session session,  _Document doc, _WebFormData webFormData, String lang);
	
}
