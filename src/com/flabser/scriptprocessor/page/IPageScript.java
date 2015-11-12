package com.flabser.scriptprocessor.page;

import com.flabser.exception.WebFormValueException;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.servlets.SessionCooks;
import com.flabser.util.ScriptResponse;

public interface IPageScript {

	void setSession(_Session ses);

	void setFormData(_WebFormData formData);

	void setMethod(String method);

	void setCurrentLang(Vocabulary vocabulary, String lang);

	void setCooks(SessionCooks cooks);

	ScriptResponse process() throws WebFormValueException;
}
