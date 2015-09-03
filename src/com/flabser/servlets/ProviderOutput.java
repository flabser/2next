package com.flabser.servlets;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.users.UserSession;

public class ProviderOutput {
	public File xslFile;
	public boolean isValid;
	protected static final String xmlTextUTF8Header = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	protected String type;
	public BrowserType browser;
	protected StringBuffer output;
	protected UserSession userSession;
	protected HttpSession jses;
	protected String id;
	private HttpServletRequest request;

	public ProviderOutput(String type, String id, StringBuffer output, HttpServletRequest request, UserSession userSession, HttpSession jses) {
		this.type = type;
		this.id = id;
		this.output = output;
		this.request = request;
		this.jses = jses;

		browser = ServletUtil.getBrowserType(request);
		this.userSession = userSession;

	}

	public boolean prepareXSLT(AppTemplate env, String xsltFile) throws XSLTFileNotFoundException {
		boolean result;

		xslFile = new File(xsltFile);
		if (xslFile.exists()) {
			env.xsltFileMap.put(xsltFile, xslFile);
			result = true;
		} else {
			throw new XSLTFileNotFoundException(xslFile.getAbsolutePath());
		}

		return result;
	}

	public String getPlainText() {
		return output.toString();
	}

	public String getStandartOutput() {

		String queryString = request.getQueryString();
		if (queryString != null) {
			queryString = "querystring=\"" + queryString.replace("&", "&amp;") + "\"";
		} else {
			queryString = "";
		}

		return xmlTextUTF8Header + "<request " + queryString + " type=\"" + type + "\" lang=\"" + userSession.getLang() + "\" id=\"" + id
				+ "\" " + "useragent=\"" + browser + "\"  userid=\"" + userSession.currentUser.getLogin() + "\" >" + output + "</request>";
	}

}