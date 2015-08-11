package com.flabser.exception;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

	public ApplicationException(String appType, String error, Exception exp) {
		super(error);
		this.appType = appType;
		StringWriter errors = new StringWriter();
		exp.printStackTrace(new PrintWriter(errors));
		exception = errors.toString();
	}

	public ApplicationException(Response r) {
		super(r);
	}

	public String getHTMLMessage() {
		String xmlText = null;

		ExceptionXML document = new ExceptionXML(getMessage(), code, location, type, servletName, exception);
		document.setAppType(appType);
		String xslt = "webapps" + File.separator + appType + File.separator + EnvConst.ERROR_XSLT;
		File errorXslt = new File(xslt);
		if (!errorXslt.exists()) {
			errorXslt = new File("webapps" + File.separator + EnvConst.WORKSPACE_APP_NAME + File.separator + EnvConst.ERROR_XSLT);
		}

		try {
			xmlText = new SaxonTransformator().toTrans(errorXslt, document.toXML());
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
