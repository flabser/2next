package com.flabser.script;

import com.flabser.runtimeobj.page.WebDocument;

public class _WebDocument implements _IXMLContent {
	WebDocument document;
	
	public _WebDocument(_Element rootTag){		
			document = new WebDocument(rootTag.getRuntimeTag());	
	}	

	public WebDocument getDocument() {
		return document;
	}
	
	public StringBuffer toXML(){
		return document.toXML();
	}
	
	public String toString(){
		return toXML().toString();
	}
	
}
