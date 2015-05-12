package com.flabser.script;

import com.flabser.script.concurrency._AJAXHandler;
import com.flabser.script.constants._JSONTemplate;
import com.flabser.script.constants._RequestType;
import com.flabser.util.Util;



public class _JSONHandler implements _IXMLContent{
	public String id;
	
	private String jsID;
	private String jsLauncherCode;
	private _AJAXHandler classInstance;
	private String jsPopulateSpot;
	private String jsLauncherName;
	private _RequestType requestType = _RequestType.GET;
	private _JSONTemplate template;

	public _JSONHandler(String jsPopulateSpot, String jsLauncherName, _AJAXHandler cl, _JSONTemplate template) {
		this(cl.getClass().getSimpleName(), jsPopulateSpot, jsLauncherName, cl, template);
		
	}
	
	public _JSONHandler(String jsID, String jsPopulateSpot, String jsLauncherName, _AJAXHandler cl, _JSONTemplate template) {
		this.jsID = jsID;
		this.jsPopulateSpot = jsPopulateSpot;
		this.jsLauncherName = jsLauncherName;
		this.template = template;
		classInstance = cl;
		id = Util.generateRandomAsText();
		
	}
	
	public _JSONHandler(String jsID, String jsPopulateSpot, String jsLauncherName, _AJAXHandler cl, String event, _JSONTemplate template) {
		this.jsID = jsID;
		this.jsPopulateSpot = jsPopulateSpot;
		this.jsLauncherName = jsLauncherName;
		this.template = template;
		classInstance = cl;
		id = Util.generateRandomAsText();
		
	}
	
	public void setRequestType(_RequestType requestType){
		this.requestType = requestType;
	}
	
	public _AJAXHandler getInstance(){
		return classInstance;		
	}
	
	@Override
	public String toXML() throws _Exception {
		switch (template){
		case POPULATE_SPOT:
			jsLauncherCode =  "<![CDATA[" +                       
		        "$('#" + jsLauncherName + "').click(function() {" + 
		       	"$.get('Provider?type=json&id=" + id + "')]]>" +  
		        ".complete(function(data){ $('#" + jsPopulateSpot +	 "').text(data.responseText); });" +
		        "});";
			break;
		case RELOAD_PAGE:
			jsLauncherCode =  "<![CDATA[" +                       
					"$('#" + jsLauncherName + "').click(function() {" + 
					"$.get('Provider?type=json&id=" + id + "')]]>" +  
					".complete(function(data){window.location.reload(); });" +
					"});";
			break;
		case NOTHING:
			jsLauncherCode =  "<![CDATA[" +                       
					"$('#" + jsLauncherName + "').click(function() {" + 
					"$.get('Provider?type=json&id=" + id + "')]]>" +  
					"});";
			break;
		case ALERT:
			jsLauncherCode =  "<![CDATA[" +                       
					"$('#" + jsLauncherName + "').click(function() {" + 
					"$.get('Provider?type=json&id=" + id + "')]]>" +  
					".complete(function(data){ infoDialog(data.responseText); });" +
					"});";
			break;
		case URL:
			jsLauncherCode = "<![CDATA[Provider?type=json&id=" + id + "]]>";
			break;
		
		default:
			jsLauncherCode =  "<![CDATA[" +                       
					"$('#" + jsLauncherName + "').click(function() {" + 
			       	"$.get('Provider?type=json&id=" + id + "')]]>" +  
			        ".complete(function(data){ $('#" + jsPopulateSpot + "').text(data.responseText); });" +
			        "});";
			break;
		         
		} 
		
		
		
	             
		return "<js id=\"" + jsID + "\">" + jsLauncherCode + "</js>";
	}
	
}