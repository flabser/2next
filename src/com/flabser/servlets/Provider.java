package com.flabser.servlets;

import net.sf.saxon.s9api.SaxonApiException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.exception.PortalException;
import com.flabser.exception.RuleException;
import com.flabser.exception.ServerException;
import com.flabser.exception.ServerExceptionType;
import com.flabser.exception.TransformatorException;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.localization.LocalizatorException;
import com.flabser.rule.IRule;
import com.flabser.rule.Skin;
import com.flabser.rule.page.PageRule;
import com.flabser.runtimeobj.page.Page;
import com.flabser.scriptprocessor.page.DoAsyncProcessor;
import com.flabser.server.Server;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.servlets.sitefiles.AttachmentHandlerException;
import com.flabser.users.AuthFailedException;
import com.flabser.users.AuthFailedExceptionType;
import com.flabser.users.User;
import com.flabser.users.UserException;
import com.flabser.users.UserSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class Provider extends HttpServlet implements Const {
	private static final long serialVersionUID = 2352885167311108325L;
	private AppEnv env;
	private ServletContext context;

	public void init(ServletConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			env = (AppEnv) context.getAttribute("portalenv");
		} catch (Exception e) {
			Server.logger.errorLogEntry(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		// long start_time = System.currentTimeMillis();
		HttpSession jses = null;
		UserSession userSession = null;
		ProviderResult result = null;
		String disposition = null;
		AttachmentHandler attachHandler = null;

		try {
			request.setCharacterEncoding("utf-8");
			String type = request.getParameter("type");
			String id = request.getParameter("id");
			String key = request.getParameter("key");
			String onlyXML = request.getParameter("onlyxml");

			if (env != null) {
				if (id != null) {
					IRule rule = env.ruleProvider.getRule(type, id);

					if (rule != null) {
						if (!rule.isAnonymousAccessAllowed()) {
							jses = request.getSession(true);
							userSession = (UserSession) jses.getAttribute("usersession");
							if (userSession == null)
								throw new AuthFailedException(AuthFailedExceptionType.NO_USER_SESSION, null);
						} else {
							jses = request.getSession(false);
							if (jses == null) {
								jses = request.getSession(true);
								userSession = new UserSession(new User());
								jses.setAttribute("usersession", userSession);
							} else {
								userSession = (UserSession) jses.getAttribute("usersession");
								if (userSession == null) {
									userSession = new UserSession(new User());
									jses.setAttribute("usersession", userSession);
								}
							}
						}

					}

					if (type.equals("json")) {
						result = json(userSession, id);

					} else if (type.equalsIgnoreCase("page")) {
						result = page(response, request, rule, userSession);
						if (result.publishAs == PublishAsType.OUTPUTSTREAM) {
							attachHandler = new AttachmentHandler(request, response, true);
						}
						// return;

					} else if (type.equalsIgnoreCase("search")) {
						result = search(request, userSession);
					} else if (type.equalsIgnoreCase("edit")) {
						result = edit(request, rule, userSession, key);

					} else if (type.equalsIgnoreCase("save")) {
						result = save(request, response, rule, userSession, key);
					} else if (type.equalsIgnoreCase("getattach")) {
						result = getAttach(request, userSession, key);
						attachHandler = new AttachmentHandler(request, response, true);
					} else if (type.equals("delete")) {
						result = delete(request, userSession);
					} else if (type.equals("undelete")) {
						result = undelete(request, userSession);

					} else {
						String reqEnc = request.getCharacterEncoding();
						type = new String(((String) type).getBytes("ISO-8859-1"), reqEnc);
						new PortalException("Request has been undefined, type=" + type + ", id=" + id + ", key=" + key,
								env, response, ProviderExceptionType.PROVIDERERROR, PublishAsType.HTML,
								userSession.skin);
						return;
					}

					if ((userSession.browserType == BrowserType.APPLICATION && result.publishAs != PublishAsType.OUTPUTSTREAM)
							|| onlyXML != null) {
						result.publishAs = PublishAsType.XML;
						result.addHistory = false;
					}

					if (result.publishAs == PublishAsType.JSON) {
						response.setContentType("application/json;charset=utf-8");
						PrintWriter out = response.getWriter();
						out.println(result.output);
						out.close();
					} else if (result.publishAs == PublishAsType.HTML) {
						if (result.disableClientCache) {
							disableCash(response);
						}
						ProviderOutput po = new ProviderOutput(type, id, result.output, request, response, userSession,
								jses, result.title, result.addHistory);
						response.setContentType("text/html");
						Skin skin = null;
						if (po.browser == BrowserType.IPAD_SAFARI || po.browser == BrowserType.ANDROID) {
							skin = env.globalSetting.skinsMap.get("ipadandtab");
						} else {
							skin = env.globalSetting.skinsMap.get(userSession.skin);
						}
						if (po.prepareXSLT(env, skin, result.xslt)) {
							String outputContent = po.getStandartOutput();
							// long start_time = System.currentTimeMillis(); //
							// for speed debuging
							new SaxonTransformator().toTrans(response, po.xslFile, outputContent);
							// System.out.println(getClass().getSimpleName() +
							// " transformation  >>> " +
							// Util.getTimeDiffInMilSec(start_time)); // for
							// speed debuging
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
						ProviderOutput po = new ProviderOutput(type, id, result.output, request, response, userSession,
								jses, result.title, result.addHistory);
						String outputContent = po.getStandartUTF8Output();
						// System.out.println(outputContent);
						PrintWriter out = response.getWriter();
						out.println(outputContent);
						out.close();
					} else if (result.publishAs == PublishAsType.TEXT) {
						if (result.disableClientCache) {
							disableCash(response);
						}
						ProviderOutput po = new ProviderOutput(type, id, result.output, request, response, userSession,
								jses, result.title, result.addHistory);
						String outputContent = po.getPlainText();
						response.setContentType("text/text;charset=utf-8");
						response.getWriter().println(outputContent);
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
					// System.out.println(type + " " +
					// Util.getTimeDiffInMilSec(start_time));
				} else {
					// response.sendRedirect(env.globalSetting.defaultRedirectURL);
					return;
				}
			} else {
				throw new ServerException(ServerExceptionType.APPENV_HAS_NOT_INITIALIZED, "context="
						+ context.getServletContextName());
			}
		} catch (AuthFailedException e) {
			String referer = request.getRequestURI() + "?" + request.getQueryString();
			if (jses == null)
				jses = request.getSession(true);
			jses.setAttribute("callingPage", referer);

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				if (e.type == AuthFailedExceptionType.NO_USER_SESSION) {
					response.sendRedirect("Logout");
				} else {
					request.getRequestDispatcher("/Error?type=ws_auth_error").forward(request, response);
				}

			} catch (IOException e1) {
				new PortalException(e, env, response, ProviderExceptionType.INTERNAL, PublishAsType.HTML,
						userSession.skin);
			} catch (ServletException e2) {
				new PortalException(e2, env, response, ProviderExceptionType.INTERNAL, PublishAsType.HTML,
						userSession.skin);
			}
		} catch (RuleException rnf) {
			new PortalException(rnf, env, response, ProviderExceptionType.RULENOTFOUND);
		} catch (XSLTFileNotFoundException xfnf) {
			new PortalException(xfnf, env, response, ProviderExceptionType.XSLTNOTFOUND, PublishAsType.HTML,
					userSession.skin);
		} catch (IOException ioe) {
			new PortalException(ioe, env, response, PublishAsType.HTML, userSession.skin);
		} catch (IllegalStateException ise) {
			new PortalException(ise, env, response, PublishAsType.HTML, userSession.skin);
		} catch (AttachmentHandlerException e) {
			new PortalException(e, env, response, ProviderExceptionType.PROVIDERERROR, PublishAsType.HTML,
					userSession.skin);
		} catch (UserException e) {
			new PortalException(e, env, response, ProviderExceptionType.INTERNAL, PublishAsType.HTML, userSession.skin);
		} catch (SaxonApiException e) {
			new PortalException(e, env, response, ProviderExceptionType.XSLT_TRANSFORMATOR_ERROR, PublishAsType.HTML,
					userSession.skin);
		} catch (TransformatorException e) {
			new PortalException(e, env, response, ProviderExceptionType.XSLT_TRANSFORMATOR_ERROR, PublishAsType.HTML,
					userSession.skin);
		} catch (ClassNotFoundException e) {
			new PortalException(e, env, response, ProviderExceptionType.CLASS_NOT_FOUND_EXCEPTION, PublishAsType.HTML,
					userSession.skin);
		} catch (ServerException e) {
			new PortalException(e, response, ProviderExceptionType.SERVER, PublishAsType.HTML);
		} catch (Exception e) {
			new PortalException(e, response, ProviderExceptionType.INTERNAL, PublishAsType.HTML);
		}
	}

	private ProviderResult json(UserSession userSession, String id) throws RuleException, LocalizatorException,
			ClassNotFoundException {
		ProviderResult result = new ProviderResult();
		result.publishAs = PublishAsType.JSON;
		DoAsyncProcessor dap = new DoAsyncProcessor(userSession);
		result.output.append(dap.processScript(id).toString());
		return result;
	}

	private ProviderResult page(HttpServletResponse response, HttpServletRequest request, IRule rule,
			UserSession userSession) throws RuleException, UnsupportedEncodingException, ClassNotFoundException {
		PageRule pageRule = (PageRule) rule;
		ProviderResult result = new ProviderResult(pageRule.publishAs, pageRule.getXSLT());
		result.addHistory = pageRule.addToHistory;
		HashMap<String, String[]> fields = new HashMap<String, String[]>();
		Map<String, String[]> parMap = request.getParameterMap();
		fields.putAll(parMap);
		Page page = new Page(env, userSession, pageRule);
		// result.output.append(page.process(fields, request, response, id,
		// userSession, jses));
		result.output.append(page.process(fields));
		if (page.fileGenerated) {
			result.publishAs = PublishAsType.OUTPUTSTREAM;
			result.filePath = page.generatedFilePath;
			result.originalAttachName = page.generatedFileOriginalName;
		}
		return result;
	}

	private ProviderResult search(HttpServletRequest request, UserSession userSession) throws RuleException,
			UnsupportedEncodingException {
		ProviderResult result = new ProviderResult(PublishAsType.HTML, "searchres.xsl");
		int page = 0;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch (NumberFormatException nfe) {
			page = 1;
		}
		String keyWord = request.getParameter("keyword");
		keyWord = new String(((String) keyWord).getBytes("ISO-8859-1"), "UTF-8");
		// FTSearchRequest ftRequest = new FTSearchRequest(env,
		// userSession.currentUser.getAllUserGroups(),
		// userSession.currentUser.getUserID(), keyWord, page,
		// userSession.pageSize);
		// result.output.append(ftRequest.getDataAsXML());
		result.addHistory = true;
		return result;
	}

	private ProviderResult edit(HttpServletRequest request, IRule rule, UserSession userSession, String key)
			throws RuleException, LocalizatorException, ClassNotFoundException {
		return null;

	}

	private ProviderResult save(HttpServletRequest request, HttpServletResponse response, IRule rule,
			UserSession userSession, String key) throws UnsupportedEncodingException, RuleException,
			ClassNotFoundException {
		ProviderResult result = new ProviderResult();

		/*
		 * String element = request.getParameter("element"); HashMap<String,
		 * String[]> fields = new HashMap<String, String[]>(); HashMap<String,
		 * String[]> parMap = (HashMap<String, String[]>)
		 * request.getParameterMap(); //Map<String, String[]> parMap =
		 * ServletUtil.showParametersMap(request); fields.putAll(parMap);
		 * 
		 * if (element != null && element.equalsIgnoreCase("user_profile")){
		 * XMLResponse xmlResult = new XMLResponse(ResponseType.SAVE_FORM);
		 * Employer emp = userSession.currentUser.getAppUser(); HashSet<Filter>
		 * currentFilters = new HashSet<Filter>(emp.getFilters());
		 * userSession.history.remove(currentFilters);
		 * userSession.setLang(fields.get("lang")[0], response);
		 * userSession.setPageSize(fields.get("pagesize")[0], response);
		 * userSession.flush(); emp.getUser().fillFieldsToSaveLight(fields);
		 * emp.setFilters(FilterParser.parse(fields, env)); int docID =
		 * emp.save(userSession.currentUser);
		 * 
		 * HashSet<Filter> updatedFilters = new
		 * HashSet<Filter>(emp.getFilters());
		 * currentFilters.removeAll(updatedFilters);
		 * 
		 * if (docID > -1){ xmlResult.setResponseStatus(true);
		 * xmlResult.addSignal(SignalType.RELOAD_PAGE); }else{
		 * xmlResult.setResponseStatus(false);
		 * xmlResult.setMessage("User has not saved"); }
		 * 
		 * result.output.append(xmlResult.toXML()); }else{ FormRule formRule =
		 * (FormRule) rule; if (formRule.advancedQSEnable){ fields = new
		 * HashMap<String, String[]>(); parMap = (HashMap<String, String[]>)
		 * request.getParameterMap(); fields.putAll(parMap); DocumentForm form =
		 * new DocumentForm(fields, env, formRule, userSession); int
		 * parentDocProp[] = getParentDocProp(request);
		 * result.output.append(form.save(key, fields,parentDocProp[0],
		 * parentDocProp[1], userSession.pageSize, userSession.lang).toXML());
		 * }else{ Form form = new Form(env, formRule, userSession); int
		 * parentDocProp[] = getParentDocProp(request);
		 * result.output.append(form.save(key, fields,parentDocProp[0],
		 * parentDocProp[1], userSession.pageSize, userSession.lang).toXML()); }
		 * }
		 */
		return result;
	}

	private ProviderResult getAttach(HttpServletRequest request, UserSession userSession, String key)
			throws UnsupportedEncodingException {
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

	private ProviderResult delete(HttpServletRequest request, UserSession userSession) {
		ProviderResult result = new ProviderResult();
		/*
		 * String ck = request.getParameter("ck"); List<DocID> keys =
		 * ComplexKeyParser.parse(ck); String completely =
		 * request.getParameter("typedel"); boolean completeRemove; if
		 * (completely != null && completely.equalsIgnoreCase("1")) {
		 * completeRemove = true; } else { completeRemove = false; } IDatabase
		 * dataBase = env.getDataBase(); User currentUser =
		 * userSession.currentUser; XMLResponse xmlResp =
		 * dataBase.deleteDocuments(keys, completeRemove, currentUser);
		 * result.output.append(xmlResp.toXML());
		 */
		return result;
	}

	private ProviderResult undelete(HttpServletRequest request, UserSession userSession) {
		ProviderResult result = new ProviderResult();
		/*
		 * String ck = request.getParameter("ck"); List<DocID> keys =
		 * ComplexKeyParser.parse(ck); IDatabase dataBase = env.getDataBase();
		 * User currentUser = userSession.currentUser; XMLResponse xmlResp =
		 * dataBase.unDeleteDocuments(keys, currentUser);
		 * result.output.append(xmlResp.toXML());
		 */
		return result;
	}

	private void disableCash(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	private int[] getParentDocProp(HttpServletRequest request) {
		int[] prop = new int[2];
		try {
			prop[0] = Integer.parseInt(request.getParameter("parentdocid"));
		} catch (NumberFormatException nfe) {
			prop[0] = 0;
		}
		try {
			prop[1] = Integer.parseInt(request.getParameter("parentdoctype"));
		} catch (NumberFormatException nfe) {
			prop[1] = DOCTYPE_UNKNOWN;
		}
		return prop;
	}
}
