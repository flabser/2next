package com.flabser.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.script._Session;
import com.flabser.server.Server;

public class Provider extends HttpServlet {

	private static final long serialVersionUID = 2352885167311108325L;
	private AppTemplate env;
	private ServletContext context;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			env = (AppTemplate) context.getAttribute(EnvConst.TEMPLATE_ATTR);
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession jses = null;
		_Session ses = null;
		ProviderResult result = null;

		try {
			request.setCharacterEncoding(EnvConst.SUPPOSED_CODE_PAGE);
			String onlyXML = request.getParameter("onlyxml");
			String id = request.getParameter("id");
			if (id == null) {
				id = EnvConst.DEFAULT_PAGE;
			}

			if (env != null) {
				IRule rule = env.ruleProvider.getRule(id);
				if (rule != null) {
					jses = request.getSession(false);
					ses = (_Session) jses.getAttribute(EnvConst.SESSION_ATTR);

					result = page(response, request, rule, ses);

					if (result.publishAs == PublishAsType.XML || onlyXML != null) {
						result.publishAs = PublishAsType.XML;
					}

					if (result.publishAs == PublishAsType.HTML) {
						if (result.disableClientCache) {
							disableCash(response);
						}
						ProviderOutput po = new ProviderOutput(id, result.output, request, ses, jses);
						response.setContentType("text/html");

						if (po.prepareXSLT(env, result.xslt)) {
							String outputContent = po.getStandartOutput();
							new SaxonTransformator().toTrans(response, po.xslFile, outputContent);
						} else {
							String outputContent = po.getStandartOutput();
							response.setContentType("text/xml;charset=utf-8");
							PrintWriter out = response.getWriter();
							out.println(outputContent);
							out.close();
						}
					} else if (result.publishAs == PublishAsType.XML) {
						if (result.disableClientCache) {
							disableCash(response);
						}
						response.setContentType("text/xml;charset=utf-8");
						ProviderOutput po = new ProviderOutput(id, result.output, request, ses, jses);
						String outputContent = po.getStandartOutput();
						PrintWriter out = response.getWriter();
						out.println(outputContent);
						out.close();
					} else if (result.publishAs == PublishAsType.FORWARD) {
						response.sendRedirect(result.forwardTo);
						return;
					}
				} else {
					return;
				}

			} else {
				throw new ApplicationException(context.getServletContextName(), "context has not found");
			}
		} catch (Exception e) {
			ApplicationException ae = new ApplicationException(env.templateType, e.toString(), e);
			response.setStatus(ae.getCode());
			response.getWriter().println(ae.getHTMLMessage());
		}
	}

	private ProviderResult page(HttpServletResponse response, HttpServletRequest request, IRule rule, _Session ses)
			throws RuleException, UnsupportedEncodingException, ClassNotFoundException, _Exception,
			WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		HashMap<String, String[]> fields = new HashMap<String, String[]>();
		Map<String, String[]> parMap = request.getParameterMap();
		fields.putAll(parMap);
		Page page = new Page(env, ses, pageRule, request.getMethod(), (String) request.getAttribute("appid"),
				new SessionCooks(request, response));
		result.output.append(page.process(fields).toXML(ses.getLanguage()));
		return result;
	}

	private void disableCash(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}
}
