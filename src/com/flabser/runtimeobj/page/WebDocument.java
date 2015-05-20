package com.flabser.runtimeobj.page;

import com.flabser.script._IXMLContent;


public class WebDocument implements _IXMLContent {
	private Element rootTag;
	
	WebDocument(String root){
		rootTag = new Element(root,"");		
	}
	
	public WebDocument(Element root){
		rootTag = root;		
	}
	
	public void setRootAttr(String attrName, String attrValue){
		rootTag.attrs.put(attrName, attrValue);			
	}
	
	public void setTag(Element tag){
		rootTag.tags.clear();
		rootTag.addTag(tag);
	}
	
	public StringBuffer toXML() {		
		return rootTag.toXML();
	}
	
}
