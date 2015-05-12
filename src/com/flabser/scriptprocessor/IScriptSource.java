package com.flabser.scriptprocessor;

import java.sql.Connection;
import java.util.Map;

import com.flabser.script._Session;

public interface IScriptSource {
	void setLang(String lang);
	void setFormData(Map<String, String[]> formData);
	void setConnection(Connection conn);
	void setUser(String user);	
	
	String[] simpleProcess();
	String[] sessionProcess();
	String[] sessionLangProcess();
	String[] documentProcess();
	String[] documentLangProcess();
	String providerHandlerProcess() throws Exception;
	String schedulerHandlerProcess() throws Exception;
	String patchHandlerProcess() throws Exception;
	String getConsoleOutput();
	void setSession(_Session session);
}
