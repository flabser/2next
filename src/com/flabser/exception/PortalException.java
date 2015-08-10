package com.flabser.exception;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.server.Server;
import com.flabser.servlets.ProviderExceptionType;
import com.flabser.servlets.PublishAsType;
import com.flabser.util.XMLUtil;

@SuppressWarnings("serial")
public class PortalException extends Exception {
	private Enum<?> type = ProviderExceptionType.INTERNAL;
	private AppTemplate env;
	private Source xsltSource;

	public PortalException(Exception e, HttpServletResponse response, ProviderExceptionType type, PublishAsType publishAs) {
		super(e);
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource = new StreamSource(new File("webapps" + File.separator + env.appType + File.separator + "xsl" + File.separator + "errors" + File.separator
				+ "error.xsl"));
		message(errorMessage(e), response, publishAs);
	}

	public PortalException(Exception e, AppTemplate env, HttpServletResponse response, PublishAsType publishAs) {
		super(e);
		this.env = env;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		xsltSource = getXSLT();
		message(errorMessage(e), response, publishAs);
	}

	public PortalException(String text, Exception e, AppTemplate env, HttpServletResponse response, ProviderExceptionType type, PublishAsType publishAs) {
		super(e);
		this.env = env;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource = getXSLT();
		message("<errorcontex>" + text + "</errorcontext>" + errorMessage(e), response, publishAs);
	}

	public PortalException(Exception e, AppTemplate env, HttpServletResponse response, Enum<?> type) {
		super(e);
		this.env = env;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		message(errorMessage(e), response, PublishAsType.XML);
	}

	public PortalException(Exception e, AppTemplate env, HttpServletResponse response, Enum<?> type, PublishAsType publishAs) {
		super(e);
		this.env = env;
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.setContentType("text/xml;charset=utf-8");
		this.type = type;
		xsltSource = getXSLT();
		message(errorMessage(e), response, publishAs);
	}

	public PortalException(String text, AppTemplate env, HttpServletResponse response, ProviderExceptionType type, PublishAsType publishAs) {
		super(text);
		this.env = env;
		this.type = type;
		xsltSource = getXSLT();
		message(text, response, publishAs);
	}

	private void message(String text, HttpServletResponse response, PublishAsType publishAs) {
		ServletOutputStream out;
		String xmlText;
		Server.logger.errorLogEntry(text);
		try {

			xmlText = "<?xml version = \"1.0\" encoding=\"utf-8\"?><request><error type=\"" + type + "\">" + "<message><version>" + Server.serverVersion
					+ "</version><errortext>" + XMLUtil.getAsTagValue(text) + "</errortext></message></error></request>";
			// System.out.println("xml text = "+xmlText);
			response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			if (publishAs == PublishAsType.HTML) {
				response.setContentType("text/html;charset=utf-8");
				out = response.getOutputStream();
				Source xmlSource = new StreamSource(new StringReader(xmlText));
				Result result = new StreamResult(out);
				TransformerFactory transFact = TransformerFactory.newInstance();
				Transformer trans = transFact.newTransformer(xsltSource);
				// System.out.println(PortalEnv.appID+": xsl transformation="+PortalEnv.errorXSL);
				trans.transform(xmlSource, result);
			} else {
				response.setContentType("text/xml;charset=utf-8");
				// response.sendError(550);
				out = response.getOutputStream();
				out.println(xmlText);
			}
		} catch (IOException ioe) {
			Server.logger.errorLogEntry(ioe);
		} catch (TransformerConfigurationException tce) {
			Server.logger.errorLogEntry(tce);
		} catch (TransformerException te) {
			Server.logger.errorLogEntry(te);
		}
	}

	public static String errorMessage(Exception exception) {
		String message = "";
		try {
			String addErrorMessage = getErrorStackString(exception.getStackTrace());

			message = exception.toString();

			return "<errortext>" + message + "</errortext>".replaceAll("\"", "'") + "<stack>" + addErrorMessage.replaceAll(">", "-").replaceAll("<", "-")
					+ "</stack>\n\r";
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return "";
		}
	}

	public static String getErrorStackString(StackTraceElement stack[]) {
		String addErrorMessage = "";
		for (int i = 0; i < stack.length; i++) {
			addErrorMessage = addErrorMessage + "\n" + stack[i].getClassName() + " > " + stack[i].getMethodName() + " "
					+ Integer.toString(stack[i].getLineNumber()) + "\n";
		}
		return addErrorMessage;
	}

	private Source getXSLT() {
		Source xsltSource = null;
		xsltSource = new StreamSource(new File("webapps" + File.separator + env.appType + File.separator + "xsl" + File.separator + "errors" + File.separator
				+ "error.xsl"));

		return xsltSource;
	}
}
