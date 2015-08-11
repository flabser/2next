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

import net.sf.saxon.s9api.SaxonApiException;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.env.EnvConst;
import com.flabser.exception.PortalException;
import com.flabser.exception.RuleException;
import com.flabser.exception.ServerException;
import com.flabser.exception.ServerExceptionType;
import com.flabser.exception.TransformatorException;
import com.flabser.exception.WebFormValueException;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.rule.IRule;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.script._Exception;
import com.flabser.server.Server;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.servlets.sitefiles.AttachmentHandlerException;
import com.flabser.users.UserException;
import com.flabser.users.UserSession;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession jses = null;
		UserSession userSession = null;
		ProviderResult result = null;
		String disposition = null;
		AttachmentHandler attachHandler = null;

		try {
			request.setCharacterEncoding(EnvConst.SUPPOSED_CODE_PAGE);
			String type = request.getParameter("type");
			String id = request.getParameter("id");
			String key = request.getParameter("key");
			String onlyXML = request.getParameter("onlyxml");

			if (env != null) {
				if (id != null) {
					IRule rule = env.ruleProvider.getRule(id);

					if (rule != null) {

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
							new PortalException("Request has been undefined, type=" + type + ", id=" + id + ", key=" + key, env, response,
									ProviderExceptionType.PROVIDERERROR, PublishAsType.HTML);
							return;
						}

						if (result.publishAs == PublishAsType.XML || onlyXML != null) {
							result.publishAs = PublishAsType.XML;
							result.addHistory = false;
						}

						if (result.publishAs == PublishAsType.HTML) {
							if (result.disableClientCache) {
								disableCash(response);
							}
							ProviderOutput po = new ProviderOutput(type, id, result.output, request, userSession, jses, result.addHistory);
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
							ProviderOutput po = new ProviderOutput(type, id, result.output, request, userSession, jses, result.addHistory);
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
					throw new RuleException("parameter \"id\" is not defined in request");
				}
			} else {
				throw new ServerException(ServerExceptionType.APPTEMPLATE_HAS_NOT_INITIALIZED, "context=" + context.getServletContextName());
			}

		} catch (RuleException rnf) {
			new PortalException(rnf, env, response, ProviderExceptionType.RULENOTFOUND, PublishAsType.HTML);
		} catch (XSLTFileNotFoundException xfnf) {
			new PortalException(xfnf, env, response, ProviderExceptionType.XSLTNOTFOUND, PublishAsType.HTML);
		} catch (IOException ioe) {
			new PortalException(ioe, env, response, PublishAsType.HTML);
		} catch (IllegalStateException ise) {
			new PortalException(ise, env, response, PublishAsType.HTML);
		} catch (AttachmentHandlerException e) {
			new PortalException(e, env, response, ProviderExceptionType.PROVIDERERROR, PublishAsType.HTML);
		} catch (UserException e) {
			new PortalException(e, env, response, ProviderExceptionType.INTERNAL, PublishAsType.HTML);
		} catch (SaxonApiException e) {
			new PortalException(e, env, response, ProviderExceptionType.XSLT_TRANSFORMATOR_ERROR, PublishAsType.HTML);
		} catch (TransformatorException e) {
			new PortalException(e, env, response, ProviderExceptionType.XSLT_TRANSFORMATOR_ERROR, PublishAsType.HTML);
		} catch (ClassNotFoundException e) {
			new PortalException(e, env, response, ProviderExceptionType.CLASS_NOT_FOUND_EXCEPTION, PublishAsType.HTML);
		} catch (ServerException e) {
			new PortalException(e, response, ProviderExceptionType.SERVER, PublishAsType.HTML);
		} catch (_Exception e) {
			// TODO Need to more informative handler in this case
			new PortalException(e, env, response, ProviderExceptionType.APPLICATION_ERROR, PublishAsType.HTML);
		} catch (WebFormValueException e) {
			// TODO Need to more informative handler in this case
			new PortalException(e, env, response, ProviderExceptionType.APPLICATION_ERROR, PublishAsType.HTML);
		} catch (Exception e) {
			// TODO Need to more informative handler in this case
			new PortalException(e, env, response, ProviderExceptionType.APPLICATION_ERROR, PublishAsType.HTML);
		}
	}

	private ProviderResult page(HttpServletResponse response, HttpServletRequest request, IRule rule, UserSession userSession)
			throws RuleException, UnsupportedEncodingException, ClassNotFoundException, _Exception, WebFormValueException {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		result.addHistory = pageRule.addToHistory;
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
		result.addHistory = true;
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
