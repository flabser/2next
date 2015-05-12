package com.flabser.script.events;

import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.page.AbstractPageScript;

public abstract class _DoScript extends AbstractPageScript {
	public abstract void doProcess(_Session session, _WebFormData formData, String lang);
}
