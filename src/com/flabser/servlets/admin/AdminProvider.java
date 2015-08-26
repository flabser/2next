package com.flabser.servlets.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.pool.DatabasePoolException;
import com.flabser.env.EnvConst;
import com.flabser.env.Environment;
import com.flabser.exception.ApplicationException;
import com.flabser.exception.RuleException;
import com.flabser.localization.LocalizatorException;
import com.flabser.log.LogFiles;
import com.flabser.runtimeobj.RuntimeObjUtil;
import com.flabser.server.Server;
import com.flabser.servlets.ProviderResult;
import com.flabser.servlets.PublishAsType;
import com.flabser.servlets.SaxonTransformator;
import com.flabser.servlets.ServletUtil;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.users.UserSession;

public class AdminProvider extends HttpServlet {
	public static final int pageSize = 30;

	private static final long serialVersionUID = 2352885167311108325L;
	private AppTemplate env;
	private ServletContext context;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			env = (AppTemplate) context.getAttribute("portalenv");
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
		AttachmentHandler attachHandler = null;
		ProviderResult result = null;

		try {
			request.setCharacterEncoding("utf-8");
			String type = request.getParameter("type");
			String element = request.getParameter("element");
			String id = request.getParameter("id");
			String key = request.getParameter("key");
			String app = request.getParameter("app");
			String dbID = request.getParameter("dbid");
			String onlyXML = request.getParameter("onlyxml");
			String disposition;
			if (request.getParameter("disposition") != null) {
				disposition = request.getParameter("disposition");
			} else {
				disposition = "attachment";
			}
			StringBuffer output = new StringBuffer(10000);
			boolean disableClientCache = false;

			System.out.println("Web request type=" + type + ", element=" + element + ", key=" + key);
			HttpSession jses = request.getSession(true);
			jses.setAttribute("lang", "EN");
			jses.setAttribute("skin", "");
			UserSession userSession = (UserSession) jses.getAttribute(EnvConst.SESSION_ATTR);

			if (jses.getAttribute("adminLoggedIn") == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}

			if (type != null) {
				if (type.equalsIgnoreCase("view")) {
					result = view(request, dbID, app, element, id);
				} else if (type.equalsIgnoreCase("edit")) {
					result = edit(request, app, element, id, key);
				} else if (type.equalsIgnoreCase("save")) {
					result = save(request, app, dbID, element, id);
				} else if (type.equalsIgnoreCase("delete")) {
					// result = delete(request, app, element, id);
				} else if (type.equalsIgnoreCase("service")) {
					result = service(request, app, id, key);
				}
			} else {
				ApplicationException ae = new ApplicationException(env.templateType, "Request is incorrect(type=null)");
				response.setStatus(ae.getCode());
				response.getWriter().println(ae.getHTMLMessage());
			}

			if (disableClientCache) {
				response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			if (onlyXML != null) {
				result.publishAs = PublishAsType.XML;
			}

			AdminProviderOutput po = new AdminProviderOutput(type, element, id, result.output, request, userSession, jses, dbID);

			if (result.publishAs == PublishAsType.HTML) {
				if (result.disableClientCache) {
					// disableCash(response);
				}

				response.setContentType("text/html");

				if (po.prepareXSLT(result.xslt)) {
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
				}
				response.setContentType("text/xml;charset=utf-8");

				String outputContent = po.getStandartOutput();
				PrintWriter out = response.getWriter();
				out.println(outputContent);
				out.close();
			} else if (result.publishAs == PublishAsType.TEXT) {
				String outputContent = po.getPlainText();
				response.setContentType("text/text;charset=utf-8");
				response.getWriter().println(outputContent);
			} else if (result.publishAs == PublishAsType.OUTPUTSTREAM) {
				if (request.getParameter("disposition") != null) {
					disposition = request.getParameter("disposition");
				} else {
					disposition = "attachment";
				}
				attachHandler = new AttachmentHandler(request, response, true);
				attachHandler.publish(result.filePath, result.originalAttachName, disposition);
			} else if (result.publishAs == PublishAsType.FORWARD) {
				response.sendRedirect(result.forwardTo);
				return;
			}

		} catch (Exception e) {
			ApplicationException ae = new ApplicationException(env.templateType, e.toString(), e);
			response.setStatus(ae.getCode());
			response.getWriter().println(ae.getHTMLMessage());
		}
	}

	private ProviderResult view(HttpServletRequest request, String dbID, String app, String element, String id) {
		ProviderResult result = new ProviderResult();
		result.publishAs = PublishAsType.HTML;
		ServiceHandler sh = null;
		String content = "";
		AppTemplate env = null;
		IDatabase db = null;
		if (app != null && !"".equalsIgnoreCase(app)) {
			env = Environment.getAppTemplate(app);

			sh = new ServiceHandler(dbID);
		} else if (dbID != null && !"".equalsIgnoreCase(dbID)) {

		} else {
			sh = new ServiceHandler();
		}

		int count = 0;
		int page = ServletUtil.getPage(request);

		if (element.equalsIgnoreCase("cfg")) {
			result.xslt = "forms" + File.separator + "cfg.xsl";
			content = sh.getCfg();
		} else if (element.equalsIgnoreCase("logs")) {
			LogFiles logs = new LogFiles();
			result.xslt = "views" + File.separator + "logs_list.xsl";
			count = logs.getCount();
			content = sh.getLogsListWrapper(logs);
		} else if (element.equalsIgnoreCase("users")) {
			result.xslt = "views" + File.separator + "users_list.xsl";
			UserServices us = new UserServices();
			String keyWord = request.getParameter("keyword");
			content = us.getUserListWrapper(keyWord, page, pageSize);
			count = us.getCount();
		} else if (element.equalsIgnoreCase("scheduler")) {
			result.xslt = "views" + File.separator + "scheduler_list.xsl";

		} else if (element.equalsIgnoreCase("backup")) {

		} else if (element.equalsIgnoreCase("activity")) {
			result.xslt = "views" + File.separator + "activity.xsl";
			// IUsersActivity ua = db.getUserActivity();
			/*
			 * count = ua.getAllActivityCount(); content =
			 * ua.getAllActivity(db.calcStartEntry(page, pageSize),
			 * pageSize).toString();
			 */

		} else if (element.equalsIgnoreCase("pages")) {
			result.xslt = "views" + File.separator + "pages_list.xsl";
			// RuleServices rs = new RuleServices();
			// content = rs.getPageRuleList(page, app, false);
		} else if (element.equalsIgnoreCase("settings")) {
			result.xslt = "forms" + File.separator + "settings.xsl";
			content = sh.getSettings(env);

		}

		result.output.append("<view count=\"" + count + "\" currentpage=\"" + page + "\" maxpage=\""
				+ RuntimeObjUtil.countMaxPage(count, pageSize) + "\">" + content + "</view>");
		return result;
	}

	private ProviderResult edit(HttpServletRequest request, String app, String element, String id, String key)
			throws NumberFormatException, RuleException, LocalizatorException {
		ProviderResult result = new ProviderResult();
		result.publishAs = PublishAsType.HTML;
		ServiceHandler sh = null;

		sh = new ServiceHandler();

		result.output.append("<document>");
		if (element.equalsIgnoreCase("cfg")) {
			result.xslt = "forms" + File.separator + "cfg.xsl";
			result.output.append(sh.getCfg());
		} else if (element.equalsIgnoreCase("log")) {
			LogFiles logs = new LogFiles();
			// result.attachHandler = new AttachmentHandler(request, response,
			// true);
			result.filePath = logs.logDir + File.separator + id;
			result.originalAttachName = id;
			result.publishAs = PublishAsType.OUTPUTSTREAM;
		} else if (element.equalsIgnoreCase("user")) {
			result.xslt = "forms" + File.separator + "user.xsl";
			UserServices us = new UserServices();
			if (key == null || key.equals("")) {
				result.output.append(us.getBlankUserAsXML());
			} else {
				result.output.append(us.getUserAsXML(key));
			}
		} else if (element.equalsIgnoreCase("schedule")) {

		} else if (element.equalsIgnoreCase("page_rule")) {

		}
		result.output.append("</document>");
		return result;

	}

	private ProviderResult save(HttpServletRequest request, String app, String dbID, String element, String id) {
		ProviderResult result = new ProviderResult();
		/*
		 * XMLResponse xmlResp = new XMLResponse(ResponseType.SAVE_FORM,true);
		 *
		 * if (element.equalsIgnoreCase("document")){ //Map<String, String[]>
		 * fields = ServletUtil.showParametersMap(request); Map<String,
		 * String[]> fields = request.getParameterMap(); IDatabase db =
		 * DatabaseFactory.getDatabaseByName(dbID); String docID =
		 * request.getParameter("docid"); BaseDocument baseDoc =
		 * db.getDocumentByDdbID(docID, Const.supervisorGroupAsSet,
		 * Const.sysUser); Document doc = (Document)baseDoc;
		 *
		 *
		 * doc.clearReaders(); for(String r: fields.get("reader")){
		 * doc.addReader(r); }
		 *
		 * doc.clearEditors(); for(String e: fields.get("editor")){
		 * doc.addEditor(e); }
		 *
		 * doc.save(new User(Const.sysUser)); }else if
		 * (element.equalsIgnoreCase("user_profile")) { UserServices us = new
		 * UserServices(); result.output.append(new
		 * XMLResponse(ResponseType.SAVE_FORM_OF_USER_PROFILE
		 * ,us.saveUser(ServletUtil.showParametersMap(request))).toXML()); }else
		 * if (element.equalsIgnoreCase("handler_rule")) { WebRuleProvider wrp =
		 * Environment.getApplication(app).ruleProvider; HandlerRule rule =
		 * (HandlerRule) wrp.getRule(HANDLER_RULE, id); if (rule != null){
		 *
		 * @SuppressWarnings("unchecked")
		 *
		 * Map<String, String[]> parMap =
		 * ServletUtil.showParametersMap(request);
		 * rule.setScript(parMap.get("script")[0].replace("&lt;",
		 * "<").replace("&gt;", ">"));
		 * rule.setDescription(parMap.get("description")[0]); } }else{
		 * xmlResp.resultFlag = false; } result.output.append(xmlResp.toXML());
		 */
		return result;
	}

	private ProviderResult service(HttpServletRequest request, String app, String id, String key) throws ClassNotFoundException,
	SQLException, InstantiationException, IllegalAccessException, DatabasePoolException {
		ProviderResult result = new ProviderResult();
		String operation = request.getParameter("operation");
		if (operation.equalsIgnoreCase("deploy")) {
			UserServices us = new UserServices();
			result.output.append(us.deploy(key));
		} else if (operation.equalsIgnoreCase("remove")) {
			UserServices us = new UserServices();
			result.output.append(us.remove(key));
		}
		return result;
	}

}
