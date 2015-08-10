package com.flabser.servlets;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.saxon.s9api.SaxonApiException;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.exception.ExceptionXML;
import com.flabser.exception.TransformatorException;
import com.flabser.server.Server;

public class Error extends HttpServlet {
	private static final long serialVersionUID = 1207733369437122383L;
	private AppTemplate env;
	private ServletContext context;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			env = (AppTemplate) context.getAttribute(AppTemplate.TEMPLATE_ATTR);
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String errorMessage = "", location = "", type = "", servletName = "", exception = "";
		Integer statusCode = 0;

		if (request.getAttribute("errorType") == null) {
			errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
			statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			location = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
			type = (String) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
			servletName = (String) request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
			exception = (String) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		} else {

		}

		ExceptionXML xml = new ExceptionXML(errorMessage, statusCode, location, type, servletName, exception);
		String xslt = "webapps" + File.separator + env.appType + File.separator + "xsl" + File.separator + "errors" + File.separator
				+ "error.xsl";

		try {
			request.setCharacterEncoding(EnvConst.supposedCodePage);
			String outputContent = xml.toXML();
			// System.out.println(outputContent);
			response.setContentType("text/html");
			File errorXslt = new File(xslt);
			new SaxonTransformator().toTrans(response, errorXslt, outputContent);
		} catch (UnsupportedEncodingException e) {
			Server.logger.errorLogEntry(e);
		} catch (IOException e) {
			Server.logger.errorLogEntry(e);
		} catch (SaxonApiException e) {
			Server.logger.errorLogEntry(e);
		} catch (TransformatorException e) {
			Server.logger.errorLogEntry(e);
		}

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

}
