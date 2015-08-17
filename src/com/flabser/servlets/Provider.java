package com.flabser.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.exception.ServerException;
import com.flabser.exception.ServerExceptionType;
import com.flabser.exception.WebFormValueException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.server.Server;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.users.UserSession;

public class Provider extends HttpServlet {
	private static final long serialVersionUID = 2352885167311108325L;
	private AppTemplate env;
	private ServletContext context;
	private static final int MONTH_TIME = 60 * 60 * 24 * 365;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession jses = null;
		UserSession userSession = null;
		ProviderResult result = null;
		String disposition = null;
		AttachmentHandler attachHandler = null;

		try {
			request.setCharacterEncoding(EnvConst.SUPPOSED_CODE_PAGE);
			String type = request.getParameter("type");
			String key = request.getParameter("key");
			String onlyXML = request.getParameter("onlyxml");
			String id = request.getParameter("id");
			if (id == null) {
				id = EnvConst.DEFAULT_PAGE;
			}

			if (env != null) {
				IRule rule = env.ruleProvider.getRule(id);
				if (rule != null) {
					SessionCooksValues cooks = new SessionCooksValues(request);
					jses = request.getSession(false);
					userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);

					if (type == null || type.equalsIgnoreCase("page")) {
						result = page(response, request, rule, userSession);
						if (result.publishAs == PublishAsType.OUTPUTSTREAM) {
							attachHandler = new AttachmentHandler(request, response, true);
						}
					} else if (type.equalsIgnoreCase("search")) {
						result = search(request, userSession);
					} else if (type.equalsIgnoreCase("getattach")) {
						result = getAttach(request, userSession, key);
						attachHandler = new AttachmentHandler(request, response, true);
					} else {
						String reqEnc = request.getCharacterEncoding();
						type = new String(type.getBytes("ISO-8859-1"), reqEnc);
						ApplicationException ae = new ApplicationException(env.appType, "Request has been undefined, type=" + type
								+ ", id=" + id + ", key=" + key);
						response.setStatus(ae.getCode());
						response.getWriter().println(ae.getHTMLMessage());
						return;
					}

					if (result.publishAs == PublishAsType.XML || onlyXML != null) {
						result.publishAs = PublishAsType.XML;
					}

					if (cooks.currentLang.equalsIgnoreCase(userSession.getLang())) {
						Cookie c = new Cookie(EnvConst.LANG_COOKIE_NAME, userSession.getLang());
						c.setMaxAge(MONTH_TIME);
						c.setDomain("/");
						response.addCookie(c);
					}

					if (result.publishAs == PublishAsType.HTML) {
						if (result.disableClientCache) {
							disableCash(response);
						}
						ProviderOutput po = new ProviderOutput(type, id, result.output, request, userSession, jses);
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
						ProviderOutput po = new ProviderOutput(type, id, result.output, request, userSession, jses);
						String outputContent = po.getStandartOutput();
						PrintWriter out = response.getWriter();
						out.println(outputContent);
						out.close();
					} else if (result.publishAs == PublishAsType.OUTPUTSTREAM) {
						if (request.getParameter("disposition") != null) {
							disposition = request.getParameter("disposition");
						} else {
							disposition = "attachment";
						}
						attachHandler.publish(result.filePath, result.originalAttachName, disposition);
					} else if (result.publishAs == PublishAsType.FORWARD) {
						response.sendRedirect(result.forwardTo);
						return;
					}
				} else {
					return;
				}

			} else {
				throw new ServerException(ServerExceptionType.APPTEMPLATE_HAS_NOT_INITIALIZED, "context=" + context.getServletContextName());
			}
		} catch (Exception e) {
			ApplicationException ae = new ApplicationException(env.appType, e.toString(), e);
			response.setStatus(ae.getCode());
			response.getWriter().println(ae.getHTMLMessage());
		}
	}

	private ProviderResult page(HttpServletResponse response, HttpServletRequest request, IRule rule, UserSession userSession)
			throws RuleException, UnsupportedEncodingException, ClassNotFoundException, _Exception, WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		HashMap<String, String[]> fields = new HashMap<String, String[]>();
		Map<String, String[]> parMap = request.getParameterMap();
		fields.putAll(parMap);
		Page page = new Page(env, userSession, pageRule, request.getMethod());
		result.output.append(page.process(fields).toXML());
		if (page.fileGenerated) {
			result.publishAs = PublishAsType.OUTPUTSTREAM;
			result.filePath = page.generatedFilePath;
			result.originalAttachName = page.generatedFileOriginalName;
		}
		return result;
	}

	private ProviderResult search(HttpServletRequest request, UserSession userSession) throws RuleException, UnsupportedEncodingException {
		ProviderResult result = new ProviderResult(PublishAsType.HTML, "searchres.xsl");
		try {
			Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException nfe) {
		}
		String keyWord = request.getParameter("keyword");
		keyWord = new String(keyWord.getBytes("ISO-8859-1"), "UTF-8");
		// FTSearchRequest ftRequest = new FTSearchRequest(env,
		// userSession.currentUser.getAllUserGroups(),
		// userSession.currentUser.getUserID(), keyWord, page,
		// userSession.pageSize);
		// result.output.append(ftRequest.getDataAsXML());
		return result;
	}

	private ProviderResult getAttach(HttpServletRequest request, UserSession userSession, String key) throws UnsupportedEncodingException {
		ProviderResult result = new ProviderResult(PublishAsType.OUTPUTSTREAM, null);
		/*
		 * String fieldName = request.getParameter("field"); String attachName =
		 * request.getParameter("file");
		 * 
		 * String reqEnc = request.getCharacterEncoding();
		 * result.originalAttachName = new
		 * String(((String)attachName).getBytes("ISO-8859-1"),reqEnc);
		 * 
		 * String formSesID = request.getParameter("formsesid"); if (formSesID
		 * != null){ File file = Util.getExistFile(result.originalAttachName,
		 * Environment.tmpDir + File.separator + formSesID + File.separator +
		 * fieldName + File.separator); if (!file.exists()){ IDatabase dataBase
		 * = env.getDataBase(); String docTypeAsText =
		 * request.getParameter("doctype"); //result.filePath =
		 * dataBase.getDocumentAttach(Integer.parseInt(key),
		 * Integer.parseInt(docTypeAsText),
		 * userSession.currentUser.getAllUserGroups(), fieldName,
		 * result.originalAttachName); result.filePath =
		 * dataBase.getDocumentAttach(Integer.parseInt(key),
		 * Integer.parseInt(docTypeAsText), fieldName,
		 * result.originalAttachName); }else{ result.filePath =
		 * file.getAbsolutePath(); } }else{ IDatabase dataBase =
		 * env.getDataBase(); String docTypeAsText =
		 * request.getParameter("doctype"); result.filePath =
		 * dataBase.getDocumentAttach(Integer.parseInt(key),
		 * Integer.parseInt(docTypeAsText),
		 * userSession.currentUser.getAllUserGroups(), fieldName,
		 * result.originalAttachName); }
		 */
		return result;
	}

	private void disableCash(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}
}
