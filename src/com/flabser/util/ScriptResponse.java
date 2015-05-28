package com.flabser.util;

import java.util.ArrayList;
import com.flabser.runtimeobj.page.Element;

public class ScriptResponse {
	public ResponseType type;
	public boolean resultFlag;
	private ArrayList<Element> elementsList = new ArrayList<Element>();
	private String elapsed_time;
	
	public ScriptResponse(ResponseType type) {
		this.type = type;
	}

	public ScriptResponse(ResponseType type, boolean b) {
		this.type = type;
		setResponseStatus(b);
	}

	public ScriptResponse(Exception e) {
		resultFlag = false;
	}

	public void setResponseType(ResponseType type) {
		this.type = type;
	}
	
	public void setResponseStatus(boolean responseStatus) {
		resultFlag = responseStatus;		
	}
	
	public ArrayList<Element> getElements(){
		return elementsList;
	}
	
	public void setPublishResult(ArrayList<Element> pulishElement) {
		this.elementsList = pulishElement;
	}

	public void setElapsedTime(String et) {
		elapsed_time = "elapsed_time = \"" + et + "\"";
	}
	
}
