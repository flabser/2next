package com.flabser.servlets;

public class ProviderResult{
	public StringBuffer output = new StringBuffer(10000);
	public PublishAsType publishAs = PublishAsType.XML;
	public String forwardTo;
	public String xslt;
	public String title;
	public boolean disableClientCache;
	public String filePath;
	public String originalAttachName;
	public boolean addHistory;

	ProviderResult(PublishAsType publishAs, String xslt){
		this.publishAs = publishAs;
		this.xslt = xslt;
	}

	public ProviderResult() {

	}



}