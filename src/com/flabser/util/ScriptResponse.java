package com.flabser.util;

import java.util.ArrayList;

import com.flabser.script._IContent;

public class ScriptResponse {
	public ResponseType type;
	public boolean resultFlag;
	private ArrayList<_IContent> elementsList;

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

	public void setPublishResult(ArrayList<_IContent> pulishElement) {
		this.elementsList = pulishElement;
	}

	public ArrayList<_IContent> getElements() {
		return elementsList;
	}
}
