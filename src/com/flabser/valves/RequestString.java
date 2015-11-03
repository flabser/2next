package com.flabser.valves;

public class RequestString {

	private String url;
	private String pageID = "";

	public RequestString(String url) {

	}

	public boolean isResource() {
		return false;

	}

	public boolean isREST() {
		return false;

	}

	public boolean isFreeREST() {
		return false;

	}

	public boolean isPage() {
		return false;
	}

	public String getPageID() {
		return pageID;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return url;
	}
}
