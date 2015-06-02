package com.flabser.servlets;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.flabser.appenv.AppEnv;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.users.UserException;
import com.flabser.users.UserSession;

public class ProviderOutput{
	public File xslFile;
	public boolean isValid;
	protected static final String xmlTextUTF8Header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	protected String type;
	public BrowserType browser;
	protected StringBuffer output;
	protected String historyXML = "";
	protected UserSession userSession;
	protected HttpSession jses;	
	protected String id;
	private HttpServletRequest request;


	public ProviderOutput(String type, String id, StringBuffer output, HttpServletRequest request, UserSession userSession, HttpSession jses, boolean addHistory) throws UserException{		
		this.type = type;
		this.id = id;		
		this.output = output;
		this.request = request;
		this.jses = jses;

		browser = ServletUtil.getBrowserType(request);
		
		this.userSession = userSession;	


		if (addHistory) addHistory();
		if (userSession.history != null) {
		/*	for(UserSession.HistoryEntry entry: userSession.history.getEntries()){				
				historyXML += "<entry type=\"" + entry.type + "\" title=\"" + entry.title + "\">" + XMLUtil.getAsTagValue(entry.URLforXML) + "</entry>";
			}*/
		}

	}



	public boolean prepareXSLT(AppEnv env, String xsltFile) throws XSLTFileNotFoundException{		
		boolean result;
	
			xslFile = new File(xsltFile);
			if (xslFile.exists()){								
				env.xsltFileMap.put(xsltFile, xslFile);
				result = true;
			}else{
				throw new XSLTFileNotFoundException(xslFile.getAbsolutePath());
			}
		
		
		return result;
	}

	public String getPlainText(){
		return output.toString();
	}

	public String getStandartOutput(){

		String queryString = request.getQueryString();
		if (queryString != null){
			queryString = "querystring=\"" + queryString.replace("&","&amp;") + "\"";
		}else{
			queryString = "";
		}

		return xmlTextUTF8Header + "<request " + queryString + " type=\"" + type + "\" lang=\"" + userSession.lang + "\" id=\"" + id + "\" " +
		"useragent=\"" + browser + "\"  userid=\"" + userSession.currentUser.getUserID() + "\" >" +
		"<history>" + historyXML + "</history>" + output + "</request>";
	}

	public String getStandartUTF8Output(){
		String localUserName = "";				
		localUserName = userSession.currentUser.getUserName();	

		String queryString = request.getQueryString();
		if (queryString != null){
			queryString = "querystring=\"" + queryString.replace("&","&amp;") + "\"";
		}else{
			queryString = "";
		}

		
		String outputContent = xmlTextUTF8Header + "<request " + queryString + " type=\"" + type + "\"  lang=\"" + userSession.lang + "\" id=\"" + id + "\" " +
				"useragent=\"" + browser + "\"  userid=\"" +  "\" " +
				"username=\"" + localUserName + "\"><history>" + historyXML + "</history>" + output + "</request>";
				
		return outputContent;
	}
	
	protected void addHistory() throws UserException{
		String ref = request.getRequestURI() + "?" + request.getQueryString();	
		try {
			userSession.addHistoryEntry(type, ref);
		} catch (org.omg.CORBA.UserException e) {
			e.printStackTrace();
		}	
	}

	

}