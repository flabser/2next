package com.flabser.script.events;

import com.flabser.exception.WebFormValueException;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.page.AbstractPageScript;

public abstract class _DoScript extends AbstractPageScript {

	@Override
	public abstract void doGet(_Session session, _WebFormData formData, String lang) throws WebFormValueException, _Exception;

	@Override
	public abstract void doPost(_Session session, _WebFormData formData, String lang) throws WebFormValueException;

	@Override
	public abstract void doPut(_Session session, _WebFormData formData, String lang) throws WebFormValueException;

	@Override
	public abstract void doDelete(_Session session, _WebFormData formData, String lang) throws WebFormValueException;
}
