package com.flabser.runtimeobj.xml;


public class XMLDocument {
	private Tag rootTag;
	
	XMLDocument(String root){
		rootTag = new Tag(root,"");		
	}
	
	public XMLDocument(Tag root){
		rootTag = root;		
	}
	
	public void setRootAttr(String attrName, String attrValue){
		rootTag.attrs.put(attrName, attrValue);			
	}
	
	public void setTag(Tag tag){
		rootTag.tags.clear();
		rootTag.addTag(tag);
	}
	
	public String toXML() {		
		return rootTag.toXML();
	}
	
}
