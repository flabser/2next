package com.flabser.scriptprocessor.page;

import java.util.ArrayList;

import com.flabser.localization.Vocabulary;
import com.flabser.runtimeobj.page.Element;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.util.Response;

public interface IPageScript {	
	void setSession(_Session ses);	
	void setFormData(_WebFormData formData);
	void setCurrentLang(Vocabulary vocabulary, String lang);
	Response process();

	
}
