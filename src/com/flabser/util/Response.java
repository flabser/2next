package com.flabser.util;

import java.util.ArrayList;
import java.util.Collection;

import com.flabser.runtimeobj.page.Element;


public class Response {
	public ResponseType type;
	public boolean resultFlag;
	private String responseStatus = "undefined";
	private ArrayList<Element> elementsList = new ArrayList<Element>();
	private String elapsed_time = "";

	public Response(ResponseType type) {
		this.type = type;
	}

	public Response(ResponseType type, boolean b) {
		this.type = type;
		setResponseStatus(b);
	}

	public Response(Exception e) {
		resultFlag = false;
		responseStatus = "error";	
	}

	public void setResponseType(ResponseType type) {
		this.type = type;
	}
	
	public void setResponseStatus(boolean responseStatus) {
		resultFlag = responseStatus;
		if (responseStatus) {
			this.responseStatus = "ok";
		} else {
			this.responseStatus = "error";
		}
	}
	
	public ArrayList<Element> getElements(){
		return elementsList;
	}
	
	public void setPublishResult(ArrayList<Element> pulishElement) {
		this.elementsList = pulishElement;
	}

	public void addXMLDocumentElements(Collection<Element> documents) {
		elementsList.addAll(documents);
	}

	public void setElapsedTime(String et) {
		elapsed_time = "elapsed_time = \"" + et + "\"";
	}

	public String toXML() {
		StringBuffer result = new StringBuffer(100);
		result.append("<response type=\"" + type + "\" status=\"" + responseStatus + "\" " + elapsed_time + ">");
		
		
		if (elementsList != null)
			result.append("<content>");

		for (Element content : elementsList) {
			result.append(content.toXML());
		}
		result.append("</content>");

		
		result.append("</response>");
		return result.toString();
	}
	

	public void setFormSesID(String formSesID) {
		// TODO Auto-generated method stub
		
	}

	public void addScript(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	
}
