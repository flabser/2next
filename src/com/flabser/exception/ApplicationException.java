package com.flabser.exception;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import net.sf.saxon.s9api.SaxonApiException;

import com.flabser.env.EnvConst;
import com.flabser.server.Server;
import com.flabser.servlets.SaxonTransformator;

public class ApplicationException extends WebApplicationException {
	private static final long serialVersionUID = 1L;
	private int code = HttpServletResponse.SC_BAD_REQUEST;
	private String location;
	private String type = "APPLICATION";
	private String servletName = "";
	private String exception;
	private String appType;

	public ApplicationException(String appType, String error) {
		super(error);
		this.appType = appType;
	}

	public ApplicationException(Response r) {
		super(r);
	}

	public String getHTMLMessage() {
		String xmlText = null;

		ExceptionXML xml = new ExceptionXML(getMessage(), code, location, type, servletName, exception);
		String xslt = "webapps" + File.separator + appType + File.separator + EnvConst.ERROR_XSLT;
		File errorXslt = new File(xslt);
		try {
			xmlText = new SaxonTransformator().toTrans(errorXslt, xml.toXML());
		} catch (IOException | SaxonApiException e) {
			Server.logger.errorLogEntry(e);
		}

		return xmlText;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setType(String type) {
		this.type = type;
	}
}
