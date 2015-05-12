package com.flabser.scriptprocessor.page;

import com.flabser.appenv.AppEnv;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;


public interface IQueryOpenScript {	
	void setAppEnv(AppEnv env);
	void setSession(_Session ses);	
	void setFormData(_WebFormData formData);
	void setCurrentLang(Vocabulary vocabulary, String lang);
	void stopOpen();
	PublishResult process1();
	PublishResult process2();
	void setDocument(_Document doc);
	
}
