package com.flabser.script;

import com.flabser.runtimeobj.xml.XMLDocument;


public class _XMLDocument implements _IXMLContent {
	XMLDocument document;
	
	public _XMLDocument(_Tag rootTag){		
			document = new XMLDocument(rootTag.getRuntimeTag());	
	}
	

	public XMLDocument getDocument() {
		return document;
	}
	
	public String toXML(){
		return document.toXML();
	}
	
	public String toString(){
		return toXML();
	}
	
}
