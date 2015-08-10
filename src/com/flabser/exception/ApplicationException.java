package com.flabser.exception;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.flabser.server.Server;

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception realException;

	public ApplicationException(String error) {
		super(error);
	}

	public ApplicationException(AuthFailedExceptionType err, HttpServletResponse response, String dir) {
		// message(err, response, dir);
	}

	public Exception getException() {
		return realException;
	}

	public String toXML() {
		PrintWriter out;
		String xmlText = null;
		try {
			// ExceptionXML xml = new ExceptionXML(getMessage(), statusCode,
			// location, type, servletName, exception);
			// ExceptionXML xml = new ExceptionXML(400, xmlText, xmlText,
			// xmlText);
			xmlText = "<?xml version = \"1.0\" encoding=\"utf-8\"?><request><error><type></type><version>" + Server.serverVersion
					+ "</version></error></request>";

			Source xmlSource = new StreamSource(new StringReader(xmlText));
			Source xsltSource = new StreamSource(new File("webapps" + File.separator + File.separator + "xsl" + File.separator + "errors"
					+ File.separator + "authfailed.xsl"));
			Result result = new StreamResult();

			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			trans.transform(xmlSource, result);

			// } catch (IOException ioe) {
			// Server.logger.errorLogEntry(ioe);
		} catch (TransformerConfigurationException tce) {
			Server.logger.errorLogEntry(tce);
		} catch (TransformerException te) {
			Server.logger.errorLogEntry(te);
		}
		return xmlText;
	}
}
