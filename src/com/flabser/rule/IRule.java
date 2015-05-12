package com.flabser.rule;

import java.util.Map;

public interface IRule {	
	boolean addToHistory();
	void update(Map<String, String[]> fields) throws  com.flabser.exception.WebFormValueException;
	boolean save();
	String getRuleAsXML(String app);
	void plusHit();
	String getRuleID();
	boolean isAnonymousAccessAllowed();
	
}
