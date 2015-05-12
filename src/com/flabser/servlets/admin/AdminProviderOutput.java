package com.flabser.servlets.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.env.Environment;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.servlets.ProviderOutput;
import com.flabser.users.UserException;
import com.flabser.users.UserSession;

import java.io.File;

public class AdminProviderOutput extends ProviderOutput {
	private String dbID;
	private String element;
	private static String  adminXSLTPath = Environment.primaryAppDir + "." + File.separator + "webapps" + File.separator + "Administrator" + File.separator + "xslt" + File.separator;	
	
	AdminProviderOutput(String type, String element, String id, StringBuffer output, HttpServletRequest request, HttpServletResponse response, UserSession userSession, HttpSession jses, String dbID) throws UserException{
		super(type, element, output, request, response, userSession, jses, "element=" + element + ", id=" + id, false);
		this.dbID = dbID;
		this.element = element;
	}


	public boolean prepareXSLT(String xsltFileName) throws XSLTFileNotFoundException{

		String xsltFilePath = adminXSLTPath + File.separator + xsltFileName;
		xslFile = new File(xsltFilePath);
		if (!xslFile.exists()){
			throw new XSLTFileNotFoundException(xslFile.getAbsolutePath());
		}
		return true;
	}

	public String getStandartOutput(){			
		
		return xmlTextUTF8Header + "<request type=\"" + type + "\" element=\"" + element + "\" id=\"" + id + "\">" +
		"<history>" + historyXML + "</history>" + new AdminOutline().getOutlineAsXML() + output + "</request>";
	}

}
