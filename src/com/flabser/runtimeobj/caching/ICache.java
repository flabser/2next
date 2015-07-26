package com.flabser.runtimeobj.caching;

import java.util.Map;

import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Page;

public interface ICache {
	_Page getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException, WebFormValueException;

	void flush();
}
