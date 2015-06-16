package com.flabser.script.events;

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.page.AbstractPageScript;


public abstract class _DoScript extends AbstractPageScript {

	public abstract void doGet(_Session session, _WebFormData formData, String lang);

	public abstract void doPost(_Session session, _WebFormData formData, String lang);

	public abstract void doPut(_Session session, _WebFormData formData, String lang);

	public abstract void doDelete(_Session session, _WebFormData formData, String lang);
}
