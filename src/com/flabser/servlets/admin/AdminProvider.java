package com.flabser.servlets.admin;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.IDatabase;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.env.Environment;
import com.flabser.exception.PortalException;
import com.flabser.exception.RuleException;
import com.flabser.exception.XSLTFileNotFoundException;
import com.flabser.localization.LocalizatorException;
import com.flabser.log.LogFiles;
import com.flabser.server.Server;
import com.flabser.servlets.ProviderExceptionType;
import com.flabser.servlets.ProviderResult;
import com.flabser.servlets.PublishAsType;
import com.flabser.servlets.SaxonTransformator;
import com.flabser.servlets.ServletUtil;
import com.flabser.servlets.sitefiles.AttachmentHandler;
import com.flabser.users.UserSession;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;


public class AdminProvider extends HttpServlet implements Const{
	public static final int pageSize = 30;

	private static final long serialVersionUID = 2352885167311108325L;
	private ISystemDatabase sysDb;
	private AppEnv env;
	private String defaultSkin = "pchelka";
	private ServletContext context;
	
	public void init (ServletConfig config)throws ServletException{
		sysDb = DatabaseFactory.getSysDatabase();
		try{
			context = config.getServletContext();
			env = (AppEnv) context.getAttribute("portalenv");			
		}catch (Exception e) {
			Server.logger.errorLogEntry(e);			
		}	
	}

	protected void  doGet(HttpServletRequest request, HttpServletResponse response){
		doPost(request, response);
	}

	protected void  doPost(HttpServletRequest request, HttpServletResponse response){
		AttachmentHandler attachHandler = null;
		ProviderResult result = null;

		try{
			request.setCharacterEncoding("utf-8");
			String type = request.getParameter("type");
			String element = request.getParameter("element");		
			String id = request.getParameter("id");	
			String key = request.getParameter("key");	
			String app = request.getParameter("app");
			String dbID = request.getParameter("dbid");	
			String onlyXML = request.getParameter("onlyxml");
			String disposition;
			if (request.getParameter("disposition") != null){
				disposition = request.getParameter("disposition");
			}else{
				disposition = "attachment";
			}
			StringBuffer output = new StringBuffer(10000);		
			boolean disableClientCache = false;

			System.out.println("Web request type=" + type + ", element=" + element + ", id=" + id	+ ", app=" + app + ", dbid=" + dbID);
			HttpSession jses = request.getSession(true);
			jses.setAttribute("lang","EN");
			jses.setAttribute("skin","");		
			UserSession userSession = (UserSession) jses.getAttribute("usersession");	
			
			

			if(jses.getAttribute("adminLoggedIn")==null){
				response.sendRedirect("/");
				return;
			}

			if (type != null){
				if(type.equalsIgnoreCase("view")) { 											
					result = view(request, dbID, app, element, id);	
				}else if (type.equalsIgnoreCase("edit")) {					
					result = edit(request, app,  element, id, key);				
				}else if (type.equalsIgnoreCase("save")) { 
					//result = save(request, app, dbID, element, id);
				}else if (type.equalsIgnoreCase("delete")) {
					//result = delete(request, app,  element, id);
				}else if(type.equalsIgnoreCase("service")){
					//result = service(request, app, id, key);
				} 
			}else{
				throw new PortalException("Request is incorrect(type=null)", env, response, ProviderExceptionType.PROVIDERERROR, PublishAsType.HTML, defaultSkin);
				//return;
			}

			if (disableClientCache){
				response.setHeader("Cache-Control", "no-cache, must-revalidate, private, no-store, s-maxage=0, max-age=0");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			if (onlyXML != null) result.publishAs = PublishAsType.XML;	

			AdminProviderOutput po = new AdminProviderOutput(type, element, id, result.output, request, response, userSession, jses, dbID);
			
			if (result.publishAs == PublishAsType.HTML){
				if (result.disableClientCache){
					//disableCash(response);
				}

				response.setContentType("text/html");

				if (po.prepareXSLT(result.xslt)){
					String outputContent = po.getStandartOutput();
					//	long start_time = System.currentTimeMillis();  // for speed debuging
					new SaxonTransformator().toTrans(response, po.xslFile, outputContent);
					//	System.out.println(getClass().getSimpleName() + " transformation  >>> " +  Util.getTimeDiffInMilSec(start_time));  // for speed debuging
				}else{
					String outputContent = po.getStandartOutput();
					response.setContentType("text/xml;charset=utf-8");		
					PrintWriter out = response.getWriter();
					out.println(outputContent);
					out.close();
				}
			}else if(result.publishAs == PublishAsType.XML){
				if (result.disableClientCache){
					//disableCash(response);
				}
				response.setContentType("text/xml;charset=utf-8");	

				String outputContent = po.getStandartUTF8Output();	
				//System.out.println(outputContent);
				PrintWriter out = response.getWriter();
				out.println(outputContent);
				out.close();
			}else  if(result.publishAs == PublishAsType.TEXT){
				if (result.disableClientCache){
					//disableCash(response);
				}

				String outputContent = po.getPlainText();	
				response.setContentType("text/text;charset=utf-8");		
				response.getWriter().println(outputContent);
			}else if (result.publishAs == PublishAsType.OUTPUTSTREAM){
				if (request.getParameter("disposition") != null){
					disposition = request.getParameter("disposition");
				}else{
					disposition = "attachment";
				}	
				attachHandler = new AttachmentHandler(request, response, true);
				attachHandler.publish(result.filePath, result.originalAttachName, disposition);			
			}else if (result.publishAs == PublishAsType.FORWARD){
				response.sendRedirect(result.forwardTo);
				return;
			}					


		}catch(XSLTFileNotFoundException xfnf){
			new PortalException(xfnf, env, response, PublishAsType.HTML, defaultSkin);
		}catch (IOException ioe) {
			new PortalException(ioe,env,response, PublishAsType.HTML, defaultSkin);
		}catch (Exception e) {		
			new PortalException(e,env,response, PublishAsType.HTML, defaultSkin);					
		}
	}		

	private ProviderResult view(HttpServletRequest request, String dbID, String app,  String element, String id) {
		ProviderResult result = new ProviderResult();
		result.publishAs = PublishAsType.HTML;	
		ServiceHandler sh = null;
		String content = "";
		AppEnv env = null;
		IDatabase db = null;
		if (app != null && !"".equalsIgnoreCase(app)){
			env = Environment.getApplication(app);
			
			sh = new ServiceHandler(dbID);
		}else if(dbID != null && !"".equalsIgnoreCase(dbID)){
			
		}else{
			sh = new ServiceHandler();
		}

		int count = 0;
		int page = ServletUtil.getPage(request);
		
		if (element.equalsIgnoreCase("cfg")) {
			result.xslt = "forms" + File.separator + "cfg.xsl";
			content = sh.getCfg();			
		}else if (element.equalsIgnoreCase("logs")) {
			LogFiles logs = new LogFiles();
			result.xslt = "views" + File.separator + "logs_list.xsl";
			count = logs.getCount();
			content = sh.getLogsListWrapper(logs);			
		}else if (element.equalsIgnoreCase("users")) {
			result.xslt = "views" + File.separator + "users_list.xsl";
			UserServices us = new UserServices();				
			String keyWord = request.getParameter("keyword");			
			content = us.getUserListWrapper(keyWord, page, pageSize);
			count = us.getCount();
		}else if(element.equalsIgnoreCase("scheduler")){
			result.xslt = "views" + File.separator + "scheduler_list.xsl";
			
		}else if (element.equalsIgnoreCase("backup")) {
				
		
		}else if(element.equalsIgnoreCase("activity")){
			result.xslt = "views" + File.separator + "activity.xsl";	
			//IUsersActivity ua = db.getUserActivity();
			/*count = ua.getAllActivityCount();
			content = ua.getAllActivity(db.calcStartEntry(page, pageSize), pageSize).toString();*/
		
		} else if (element.equalsIgnoreCase("pages")) {
			result.xslt = "views" + File.separator + "pages_list.xsl";
		//	RuleServices rs = new RuleServices();
		//	content = rs.getPageRuleList(page, app, false);	
		}else if(element.equalsIgnoreCase("settings")){
			result.xslt = "forms" + File.separator + "settings.xsl";				
			content = sh.getSettings(env);
		
		}

		//result.output.append("<query count=\"" + count + "\" currentpage=\"" + page + "\" maxpage=\"" + RuntimeObjUtil.countMaxPage(count, pageSize) + "\">" + content + "</query>");
		return result;
	}

	private  ProviderResult edit(HttpServletRequest request, String app,  String element, String id, String key) throws NumberFormatException, RuleException, LocalizatorException{
		ProviderResult result = new ProviderResult();
		result.publishAs =  PublishAsType.HTML;
		ServiceHandler sh = null;
		
		sh = new ServiceHandler();		

		result.output.append("<document>");
		if (element.equalsIgnoreCase("cfg")) {
			result.xslt = "forms" + File.separator + "cfg.xsl";
			result.output.append(sh.getCfg());
		}else if(element.equalsIgnoreCase("log")){						
			LogFiles logs = new LogFiles();
			//result.attachHandler = new AttachmentHandler(request, response, true);
			result.filePath = logs.logDir + File.separator + id;
			result.originalAttachName = id;
			result.publishAs = PublishAsType.OUTPUTSTREAM;	
		}else if (element.equalsIgnoreCase("user")) {
			result.xslt = "forms" + File.separator + "user.xsl";
			UserServices us = new UserServices();
			if (key == null || key.equals("")){
				result.output.append(us.getBlankUserAsXML());
			}else{
				result.output.append(us.getUserAsXML(key));	
			}
		}else if(element.equalsIgnoreCase("schedule")){						
			
		}else if(element.equalsIgnoreCase("backup")){						
		
		}else if(element.equalsIgnoreCase("handler_rule")){
		
		}else if(element.equalsIgnoreCase("page_rule")){			
		
		}
		result.output.append("</document>");
		return result;

	}

	private ProviderResult save(HttpServletRequest request, String app, String dbID, String element, String id){
		ProviderResult result = new ProviderResult();	
	/*	XMLResponse xmlResp = new XMLResponse(ResponseType.SAVE_FORM,true);
		
		if (element.equalsIgnoreCase("document")){
			//Map<String, String[]> fields = ServletUtil.showParametersMap(request);	
			Map<String, String[]> fields = request.getParameterMap();
			IDatabase db = DatabaseFactory.getDatabaseByName(dbID);
			String docID = request.getParameter("docid");			
			BaseDocument baseDoc = db.getDocumentByDdbID(docID, Const.supervisorGroupAsSet, Const.sysUser);
			Document doc = (Document)baseDoc;
			

			doc.clearReaders();
			for(String r: fields.get("reader")){
				doc.addReader(r);
			}

			doc.clearEditors();
			for(String e: fields.get("editor")){
				doc.addEditor(e);
			}

			doc.save(new User(Const.sysUser));
		}else if (element.equalsIgnoreCase("user_profile")) {
			UserServices us = new UserServices();	
			result.output.append(new XMLResponse(ResponseType.SAVE_FORM_OF_USER_PROFILE,us.saveUser(ServletUtil.showParametersMap(request))).toXML());
		}else if (element.equalsIgnoreCase("handler_rule")) {
			WebRuleProvider wrp = Environment.getApplication(app).ruleProvider;
			HandlerRule rule = (HandlerRule) wrp.getRule(HANDLER_RULE, id);
			if (rule != null){
				@SuppressWarnings("unchecked")

				Map<String, String[]> parMap = ServletUtil.showParametersMap(request);
				rule.setScript(parMap.get("script")[0].replace("&lt;", "<").replace("&gt;", ">"));
				rule.setDescription(parMap.get("description")[0]);			}
		}else{
			xmlResp.resultFlag = false;
		}
		result.output.append(xmlResp.toXML());*/
		return result;
	}

	private ProviderResult delete(HttpServletRequest request, String app,  String element, String id){
		ProviderResult result = new ProviderResult();	

		/*String dbID = "";
		if (app != null){
			dbID = Environment.getApplication(app).getDataBase().getDbID();
		}

		result.output.append("<delete>");
		if (element.equalsIgnoreCase("user")) {					
			UserServices us = new UserServices();					
			result.output.append(new XMLResponse(ResponseType.DELETE_USER,us.deleteUser(id)).toXML());
		}else if (element.equalsIgnoreCase("log")) {	
			LogFiles logs = new LogFiles();		
			String filePath = logs.logDir + File.separator + id;
			File file = new File(filePath);
			file.delete();
		}else if (element.equalsIgnoreCase("document")) {				
			int docID = Integer.parseInt(request.getParameter("docid"));
			int docType = Integer.parseInt(request.getParameter("doctype"));						
			IDatabase db = DatabaseFactory.getDatabaseByName(dbID);
			XMLResponse xmlResp;
			try {
				db.deleteDocument(docType, docID, new User(Const.sysUser, env), true);
				xmlResp = new XMLResponse(ResponseType.DELETE_DOCUMENT, true);
			} catch (Exception e) {
				xmlResp = new XMLResponse(ResponseType.DELETE_DOCUMENT, false);
			}
			result.output.append(xmlResp.toXML());	
		}

		result.output.append("</delete>");*/
		return result;
	}
	private ProviderResult service(HttpServletRequest request, String app, String id, String key) throws ClassNotFoundException{
		ProviderResult result = new ProviderResult();	
		String operation = request.getParameter("operation");
        if(operation.equalsIgnoreCase("restore_from_backup")){
            if(id != null && id.trim().length() > 0 && new File(Environment.backupDir + File.separator + id).exists()){
                File dir = new File(Environment.backupDir + File.separator + id);
                File[] listDirDocs = dir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });
              
            }

        
		}
		return result;		
	}




	

}
