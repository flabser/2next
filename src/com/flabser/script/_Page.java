package com.flabser.script;

import com.flabser.runtimeobj.page.Page;


public class _Page{
	private Page baseElement;
	private _WebFormData webFormData;
	
	public _Page(Page page, _WebFormData webFormData) {
		baseElement = page;
		this.webFormData = webFormData;
	}

	
	
}
