package com.flabser.users;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.flabser.restful.AuthUser;
import com.flabser.server.Server;

public class AuthFailedException extends WebApplicationException {
	public AuthFailedExceptionType type;

	private static final long serialVersionUID = 3214292820186296427L;
	private String errorText;

	public AuthFailedException(AuthUser user) {
		super(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, user.getError().toString()).entity(user).build());
		this.type = user.getError();
		switch (type) {
		case NO_USER_SESSION:
			errorText = "No user session";
			break;
		case PASSWORD_OR_LOGIN_INCORRECT:
			errorText = "Password or login is incorrect login=\"" + user + "\"";
			break;
		default:
			break;
		}
	}

	public AuthFailedException(AuthUser user, String addText) {
		super(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, user.getError().toString()).entity(user).build());
		errorText = addText;
	}

	public AuthFailedException(String text, HttpServletResponse response, boolean doTransform) {
		message(text, response, doTransform);
	}

	public AuthFailedException(String text) {
		super(text);
	}

	@Override
	public String getMessage() {
		return errorText;
	}

	private static void message(String text, HttpServletResponse response, boolean doTransform) {
		PrintWriter out;
		String xmlText;
		Server.logger.errorLogEntry(text);
		try {

			xmlText = "<?xml version = \"1.0\" encoding=\"utf-8\"?><request><error type=\"authfailed\">" + "<message>login: " + text + "</message><version>"
					+ Server.serverVersion + "</version></error></request>";
			response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			if (doTransform) {
				response.setContentType("text/html;charset=utf-8");
				out = response.getWriter();
				Source xmlSource = new StreamSource(new StringReader(xmlText));
				Source xsltSource = new StreamSource(new File("xsl" + File.separator + "authfailed.xsl"));
				Result result = new StreamResult(out);

				TransformerFactory transFact = TransformerFactory.newInstance();
				Transformer trans = transFact.newTransformer(xsltSource);
				trans.transform(xmlSource, result);
			} else {
				response.setContentType("text/xml;charset=Windows-1251");
				// response.sendError(550);
				out = response.getWriter();
				out.println(xmlText);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
			ioe.printStackTrace();
		} catch (TransformerConfigurationException tce) {
			System.out.println(tce);
			tce.printStackTrace();
		} catch (TransformerException te) {
			System.out.println(te);
			te.printStackTrace();
		}
	}
}
