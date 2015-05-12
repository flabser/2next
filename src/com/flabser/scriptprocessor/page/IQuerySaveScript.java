package com.flabser.scriptprocessor.page;

import com.flabser.localization.Vocabulary;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;


public interface IQuerySaveScript {	
	void setSession(_Session ses);	
	void setDocument(_Document doc);
	void setWebFormData(_WebFormData webFormData);
	@Deprecated
	void setUser(String user);
	void setCurrentLang(Vocabulary vocabulary, String lang);
	void stopSave();
	QuerySaveResult process();
	
}
