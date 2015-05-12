package com.flabser.runtimeobj.caching;

import java.util.Map;
import com.flabser.exception.RuleException;
import com.flabser.runtimeobj.page.Page;


public interface ICache {
	StringBuffer getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException;
	void flush();
}
