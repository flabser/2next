package com.flabser.env;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SAXHandler extends DefaultHandler{
	public String host;
	public String user;
	public String pwd;
	public String dominoServer;
	public String serviceDb;
	public String mailBoxDb;	
	public String maxSoftError, maxHardError, orgID, XSLTPath;
	public String inspectorIntMin;	
	private boolean isHost, isUser, isPwd, isDominoServer, isServiceDb, isMailBox, isMaxSoftError, isMaxHardError, isOrgID;
	private boolean isXSLPath;
	
	public void startElement(String uri, String name, String qname, Attributes attrs){	
		if (qname.equals("host"))isHost = true;
		if (qname.equals("user"))isUser = true;
		if (qname.equals("pwd"))isPwd = true;
		if (qname.equals("dominoserver"))isDominoServer = true;
		if (qname.equals("servicedb"))isServiceDb = true;
		if (qname.equals("mailboxdb"))isMailBox = true;	
		if (qname.equals("maxsofterror"))isMaxSoftError = true;	
		if (qname.equals("maxharderror"))isMaxHardError = true;	
		if (qname.equals("orgid"))isOrgID = true;		
		if (qname.equals("xslpath"))isXSLPath = true;
		
	}
		
	public void characters(char[] buf, int offset, int len) throws SAXException{
		String s = new String (buf, offset, len);			
		if (isHost){
			host = s;
			isHost = false;                            
		}else if (isUser){
			user = s;
			isUser = false;                            
		}else if (isPwd){
			pwd = s;
			isPwd = false;                            
		}else if (isDominoServer){
			dominoServer = s;
			isDominoServer = false;                            
		}else if (isServiceDb){
			serviceDb = s;
			isServiceDb = false;                            
		}else if (isMailBox){
			mailBoxDb = s;
			isMailBox = false;                            
		}else if (isMaxSoftError){
			maxSoftError = s;
			isMaxSoftError = false;                            
		}else if (isMaxHardError){
			maxHardError = s;
			isMaxHardError = false; 
		}else if (isOrgID){
			orgID = s;
			isOrgID = false; 
		}else if (isXSLPath){
			XSLTPath = s;
			isXSLPath = false;				
		}																																		
	}
}