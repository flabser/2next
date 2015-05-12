package com.flabser.scriptprocessor.page;

public class QuerySaveResult {
	public boolean continueSave;
	public String msg;
	@Deprecated
	public String redirectView;
	public String redirectURL;
	
	QuerySaveResult(boolean cs, String m, String v, String redirectURL){
		continueSave = cs;
		msg = m;
		redirectView = v;
		this.redirectURL = redirectURL;
	}
}
