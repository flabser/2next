package com.flabser.servlets.admin;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import com.flabser.appenv.AppEnv;
import com.flabser.dataengine.Const;
import com.flabser.env.Environment;
import com.flabser.log.LogFiles;
import com.flabser.rule.Lang;
import com.flabser.rule.Role;
import com.flabser.rule.Skin;
import com.flabser.server.Server;

public class ServiceHandler implements Const {

	private static int pageSize = 20;

	public ServiceHandler(String db){

	} 

	public ServiceHandler(){

	} 

	String getCfg()  {		
		String xmlFragment = "";
		String viewText = "";
		xmlFragment += "<version>" + Server.serverVersion + "</version>";		
		xmlFragment += "<host>" + Environment.hostName+ "</host>";
		xmlFragment += "<port>" + Environment.httpPort+ "</port>";
		xmlFragment += "<sslenable>" + Environment.isSSLEnable + "</sslenable>";
		xmlFragment += "<keystore>" + Environment.keyStore+ "</keystore>";
		xmlFragment += "<keypwd>" + Environment.keyPwd + "</keypwd>";
		xmlFragment += "<tlsauth>" + Environment.isClientSSLAuthEnable+ "</tlsauth>";
		xmlFragment += "<truststore>" + Environment.trustStore+ "</truststore>";	
		
		xmlFragment += "<tmpdir>" + Environment.tmpDir + "</tmpdir>";
		xmlFragment += "<smtphost>" + Environment.SMTPHost + "</smtphost>";
		xmlFragment += "<defaultsender>" + Environment.defaultSender + "</defaultsender>";	
		

		xmlFragment += "<applications>";
		for(AppEnv env: Environment.getApplications()){
			xmlFragment += "<entry>";
			xmlFragment += "<apptype>" + env.appType + "</apptype>";
			xmlFragment += "<implemantion>" + env.globalSetting.implemantion + "</implemantion>";
			xmlFragment += "</entry>";
		}
		xmlFragment += "</applications>";


		xmlFragment = "<document doctype = \"system\"" +		
		" viewtext=\"" + viewText +"\" >" + xmlFragment + "</document>";
		return xmlFragment;			
	}

	String getSettings(AppEnv env)  {		
		StringBuffer xmlFragment = new StringBuffer(1000);
		String viewText = "";
		xmlFragment.append("<application>" + env.globalSetting.appName + "</application>");		
		xmlFragment.append("<mode>" + env.globalSetting.isOn + "</mode>");	
		xmlFragment.append("<description>" + env.globalSetting.description + "</description>");		
		xmlFragment.append("<availablelangs>");
		for(Lang lang: env.globalSetting.langsList){
			xmlFragment.append("<entry>" + lang.toXML() + "</entry>");		
		}
		xmlFragment.append("</availablelangs>");

		xmlFragment.append("<skins>");
		for(Skin skin: env.globalSetting.skinsList){
			xmlFragment.append("<entry>" + skin.toXML() + "</entry>");		
		}
		xmlFragment.append("</skins>");

		xmlFragment.append("<roles>");
		for(Role role: env.globalSetting.roleCollection.getRolesMap().values()){
			xmlFragment.append("<entry>" + role.toXML() + "</entry>");		
		}
		xmlFragment.append("</roles>");

		
		xmlFragment.append("<xslt>");
		for(Entry<String, File> entry: env.xsltFileMap.entrySet()){
			xmlFragment.append("<entry><key>" + entry.getKey() + "</key><file>" + entry.getValue().getPath() + "</file></entry>");		
		}
		xmlFragment.append("</xslt>");
		
	

		
		xmlFragment.append("<database>");
		/*String dbAttr = "";
				dbAttr = " dbid= \""+ env.getDataBase().getDbID() + "\"  ";
			xmlFragment.append("<id>" + env.getDataBase().getDbID() + "</id>");
			xmlFragment.append("<version>" + env.getDataBase().getVersion() + "</version>");
			xmlFragment.append("<daemons>");
			
			xmlFragment.append("</daemons>");
			xmlFragment.append("<dbpool>");
			//xmlFragment.append(env.getDataBase().getConnectionPool().toXML());
			xmlFragment.append("</dbpool>");
			*/
		
		xmlFragment.append("</database>");
		
		return  xmlFragment.toString();			
	}

	
	String getLogsListWrapper(ArrayList<File> fl, int pageNum) {
		String fieldsAsXML = "";
		int length = fl.size();
	

		Iterator<File> it = fl.iterator();
		while (it.hasNext()) {
			File logFile = it.next();
			fieldsAsXML += "<entry><name>" + logFile.getName() + "</name>"
			+ "<length>" + logFile.length() + "</length>"
			+ "<lastmodified>" + logFile.lastModified()
			+ "</lastmodified>" + "</entry>";
		}

		int maxPage = length / pageSize;
		if (maxPage < 1)maxPage = 1;
		
		LogFiles logs = new LogFiles();
		return "<query count=\"" + fl.size() + "\" currentpage=\"" + pageNum
		+ "\" maxpage=\"" + maxPage + "\" path=\"" + logs.logDir
		+ "\">" + fieldsAsXML + "</query>";
	}

	public String getLogsListWrapper(LogFiles logs) {
		String fieldsAsXML = "";
		ArrayList<File> fl = logs.getLogFileList();

		Iterator<File> it = fl.iterator();
		while (it.hasNext()) {
			File logFile = it.next();
			fieldsAsXML += "<entry><name>" + logFile.getName() + "</name>"
			+ "<length>" + logFile.length() + "</length>"
			+ "<lastmodified>" + logFile.lastModified()
			+ "</lastmodified>" + "</entry>";
		}
		
		return  fieldsAsXML;
	}

}
