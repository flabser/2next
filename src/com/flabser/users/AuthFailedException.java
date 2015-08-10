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

@SuppressWarnings("serial")
public class AuthFailedException extends WebApplicationException {
	public AuthFailedExceptionType type;

	public AuthFailedException(AuthUser user) {
		super(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, user.getError().toString()).entity(user).build());
	}

	public AuthFailedException(AuthFailedExceptionType err, HttpServletResponse response, String dir) {
		message(err, response, dir);
	}

	private void message(AuthFailedExceptionType err, HttpServletResponse response, String dir) {
		PrintWriter out;
		String xmlText;
		try {

			xmlText = "<?xml version = \"1.0\" encoding=\"utf-8\"?><request><error><type>login: " + err.name() + "</type><version>" + Server.serverVersion
					+ "</version></error></request>";
			response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			response.setContentType("text/html;charset=utf-8");
			out = response.getWriter();
			Source xmlSource = new StreamSource(new StringReader(xmlText));
			Source xsltSource = new StreamSource(new File("webapps" + File.separator + dir + File.separator + "xsl" + File.separator + "errors"
					+ File.separator + "authfailed.xsl"));
			Result result = new StreamResult(out);

			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			trans.transform(xmlSource, result);

		} catch (IOException ioe) {
			Server.logger.errorLogEntry(ioe);
		} catch (TransformerConfigurationException tce) {
			Server.logger.errorLogEntry(tce);
		} catch (TransformerException te) {
			Server.logger.errorLogEntry(te);
		}
	}
}
