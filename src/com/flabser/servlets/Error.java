package com.flabser.servlets;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.saxon.s9api.SaxonApiException;

import com.flabser.apptemplate.AppTemplate;
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
		String type = request.getParameter("type");
		if (type == null) {
			type = "";
		}
		String msg = request.getParameter("msg");
		if (msg == null) {
			msg = "";
		}
		String xslt = "webapps" + File.separator + env.appType + File.separator + "xsl" + File.separator + "errors" + File.separator + "error.xsl";
		try {
			request.setCharacterEncoding("utf-8");
			String outputContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

			if (type.equals("default_url_not_defined")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				msg = "default URL has not defined in global setting";
				outputContent = outputContent + "<request><error type=\"" + type + "\">" + "<message>" + msg + "</message><version>" + Server.serverVersion
						+ "</version></error></request>";

			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				outputContent = outputContent + "<request><error type=\"" + type + "\">" + "<message>" + msg + "</message><version>" + Server.serverVersion
						+ "</version></error></request>";
			}

			response.setContentType("text/html");
			File errorXslt = new File(xslt);
			new SaxonTransformator().toTrans(response, errorXslt, outputContent);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SaxonApiException e) {
			e.printStackTrace();
		} catch (TransformatorException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

}
