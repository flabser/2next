package com.flabser.scriptprocessor.page;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import com.flabser.appenv.AppEnv;
import com.flabser.localization.SentenceCaption;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Document;
import com.flabser.script._Exception;
import com.flabser.script._ExceptionType;
import com.flabser.script._Helper;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.ScriptEvent;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import com.flabser.scriptprocessor.ScriptShowField;
import com.flabser.util.XMLUtil;

public abstract class AbstractQueryOpen extends ScriptEvent implements IQueryOpenScript {	
	private _Session ses;
	private _Document doc;
	private boolean continueOpen = true;
	private String lang;
	private ArrayList<_IXMLContent> toPublish = new ArrayList<_IXMLContent>();

	private _WebFormData webFormData;
	private AppEnv env;

	public void setAppEnv(AppEnv env){
		this.env = env;
	}

	public void setSession(_Session ses){			
		this.ses = ses;
	}

	public void setDocument(_Document doc){
		this.doc = doc;
		ses.setDocumentInConext(doc);
	}

	public void setFormData(_WebFormData webFormData){
		this.webFormData = webFormData;
	}

	public void setCurrentLang(Vocabulary vocabulary, String lang){
		this.lang = lang;
		this.vocabulary = vocabulary;
	}

	public void stopOpen(){
		this.continueOpen = false;
	}

	public void publishElement(_IXMLContent value){
		toPublishElement.add(value);
	}

	public void publishValue(String entryName, Object value) throws _Exception {		
		if (value == null) {
			toPublish.add(new ScriptShowField(entryName, ""));
		}else if (value instanceof String) {
			toPublish.add(new ScriptShowField(entryName, (String)value));
		}else if (value instanceof _IXMLContent) {
			toPublish.add(new ScriptShowField(entryName,(_IXMLContent)value));
		}else if (value instanceof Date) {
			toPublish.add(new ScriptShowField(entryName, _Helper.getDateAsString((Date)value)));
		}else if (value instanceof Enum) {
			toPublish.add(new ScriptShowField(entryName, ((Enum)value).name()));
		}else if (value instanceof BigDecimal) {
			toPublish.add(new ScriptShowField(entryName, value.toString()));
		}
	}

	public void publishValue(String entryName, Collection<_IXMLContent> col) throws _Exception{
		String entries = "";
		for (_IXMLContent o:col) {
			//entries += XMLUtil.getAsTagValue(o.toXML());
			entries += o.toXML();
		}		
		toPublish.add(new ScriptShowField(entryName, entries,true));
	}

	public void publishValue(String entryName, ArrayList <?> list) throws _Exception {
		String result = "";
		for (Object val: list){
			if(val instanceof _IXMLContent){
				result += "<entry>" +  ((_IXMLContent) val).toXML() + "</entry>";
			}else{
				result += "<entry>" +  XMLUtil.getAsTagValue(val.toString()) + "</entry>";
			}
		}
		toPublish.add(new ScriptShowField(entryName, result, true));

	}

	public void publishValue(String entryName, String value, boolean translate){
		if (translate){
			SentenceCaption s = vocabulary.getSentenceCaption(value, lang);	
			value = s.word;
		}
		toPublish.add(new ScriptShowField(entryName, value));
	}

	public void publishValue(boolean noConvert, String entryName, String value){
		toPublish.add(new ScriptShowField(entryName, value, noConvert));
	}

	public void publishValue(String entryName, int value){
		toPublish.add(new ScriptShowField(entryName, Integer.toString(value)));
	}

	public void publishValue(String entryName, double value){
		toPublish.add(new ScriptShowField(entryName, Double.toString(value)));
	}

	public void publishValue(String entryName, HashSet value) throws _Exception{
		String result = "";
		for (Object val: value){
			if(val instanceof _IXMLContent){
				result += "<entry>" +  ((_IXMLContent) val).toXML() + "</entry>";
			}else{
				result += "<entry>" +  XMLUtil.getAsTagValue(val.toString()) + "</entry>";
			}

		}
		toPublish.add(new ScriptShowField(entryName, result, true));
	}

	public void publishValue(String entryName, int idValue, String value){
		toPublish.add(new ScriptShowField(entryName, idValue, value));
	}

	public void publishGlossaryValue(String entryName, int idValue) throws _Exception{
		/*try{
			Glossary gloss =   env.getDataBase().getGlossaries().getGlossaryDocumentByID(idValue);					
			if(gloss != null){						
				toPublish.add(new ScriptShowField(entryName, idValue, gloss.getViewText().replace("&", "&amp;")));						
			}else{
				if(idValue > -1) AppEnv.logger.warningLogEntry("Value \"" + idValue + "\" of the glossary has not found");
			}
		}catch(IndexOutOfBoundsException e){
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName);
		}*/

	}

	public void publishDepartment(String entryName, ArrayList<Integer> vals) throws _Exception {
		try {
			for (Integer val : vals) {
				publishDepartment(entryName, val);
			}
		} catch (Exception e) {
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName);
		}
	}

	public void publishGlossaryValue(String entryName,  ArrayList<Integer> vals) throws _Exception{
		try{
            for (Integer val : vals) {
                publishGlossaryValue(entryName, val);
            }
		}catch(IndexOutOfBoundsException e){
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName);
		}
	}

	public void publishGlossaryValue(String entryName, String idValue) throws _Exception{
	/*	try{
			Glossary gloss =   env.getDataBase().getGlossaries().getGlossaryDocumentByID(idValue);					
			if(gloss != null){						
				toPublish.add(new ScriptShowField(entryName, idValue, gloss.getViewText().replace("&", "&amp;")));						
			}else{
				AppEnv.logger.warningLogEntry("Value \"" + idValue + "\" of the glossary has not found");
			}
		}catch(IndexOutOfBoundsException e){
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName);
		}*/
	}


	public void publishEmployer(String entryName,  String userID) throws _Exception{
		/*if (userID != null){
			Employer emp = env.getDataBase().getStructure().getAppUser(userID);

			if(emp != null){						
				toPublish.add(new ScriptShowField(entryName, userID, emp.getFullName()));							
			}else{
				AppEnv.logger.warningLogEntry("Employer  \"" + userID + "\" has not found");
			}	
		}else{
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName + ":userID of Employer is null");
		}*/
	}

	public void publishOrganization(String entryName,  int orgID) throws _Exception{
		/*if (orgID != 0){
			Organization org = env.getDataBase().getStructure().getOrganization(orgID, new User(Const.sysUser));
			if(org != null){						
				toPublish.add(new ScriptShowField(entryName, orgID, org.getFullName()));							
			}else{
				AppEnv.logger.warningLogEntry("Organization  \"" + orgID + "\" has not found");
			}	
		}else{
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName + ":orgID of Organization is null");
		}*/
	}

	public void publishDepartment(String entryName, int depID) throws _Exception {
		/*if (depID != 0) {
			Department dep = env.getDataBase().getStructure().getDepartment(depID, new User(Const.sysUser));
			if (dep != null) {
				toPublish.add(new ScriptShowField(entryName, depID, dep.getFullName()));
			} else {
				AppEnv.logger.warningLogEntry("Department \"" + depID + "\" has not found");
			}
		} else {
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName + ":depID of Department is null");
		}*/
	}

	public void publishEmployer(String entryName,  HashSet value) throws _Exception{
		if (value != null){
			toPublish.add(new ScriptShowField(entryName, getEmployersXMLPieceList(value),true));
		}else{
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName + ":Set of Employers is null");
		}
	}

	public void publishEmployer(String entryName,  Collection<String> value) throws _Exception{
		if (value != null){
			toPublish.add(new ScriptShowField(entryName, getEmployersXMLPieceList(value),true));
		}else{
			throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, entryName + ":Collection of Employers is null");
		}
	}

	public void publishAttachment(String entryName,  String fieldName) throws _Exception{
		/*_Field field = doc.getField(fieldName);
		if (field != null){
			toPublish.add(new ScriptShowField(entryName, field.toXML(), true));
		}else{
			throw new _Exception(_ExceptionType.ATTACHMENT_FIELD_NOT_FOUND, fieldName);
		}*/
	}

	public String getLocalizedWord(String word, String lang){
		return getWord(word, vocabulary, lang);
	}

	public PublishResult process1(){
		try{
			doQueryOpen(ses, webFormData, lang);		
		}catch(Throwable e){
			continueOpen = new Boolean(false);
			e.printStackTrace();
			toPublish.add(new ScriptShowField("error",e.getMessage() + ", " + ScriptProcessorUtil.getGroovyError(e.getStackTrace())));
			//msg = "QuerySaveScript: " + e.getClass().getSimpleName() + " " + e.getMessage();
		}
		PublishResult qsr = new PublishResult(continueOpen, toPublish, toPublishElement);
		return qsr;	
	}

	public PublishResult process2(){
		try{
			doQueryOpen(ses, doc, webFormData, lang);		
		}catch(Throwable e){
			continueOpen = new Boolean(false);
			e.printStackTrace();
			toPublish.add(new ScriptShowField("error",e.getMessage() + ", " + ScriptProcessorUtil.getGroovyError(e.getStackTrace())));
			//msg = "QuerySaveScript: " + e.getClass().getSimpleName() + " " + e.getMessage();
		}
		PublishResult qsr = new PublishResult(continueOpen, toPublish, toPublishElement);
		return qsr;	
	}


	public abstract void doQueryOpen(_Session ses, _WebFormData webFormData, String lang);

	public abstract void doQueryOpen(_Session ses,  _Document doc, _WebFormData webFormData, String lang);

	private String getEmployersXMLPieceList(Iterable it){
		/*IStructure struct = env.getDataBase().getStructure();
		String vals = "";
		for(Object uid:it){
			String userID = uid.toString();
			ScriptShowField field = null;
			if (userID.startsWith("[") && userID.endsWith("]")){
				UserGroup ug = struct.getGroup(userID, Const.supervisorGroupAsSet, Const.sysUser);			
				if (ug != null){
					field = new ScriptShowField("entry", ug.getName(), ug.getDescription());
				}else{
					field = new ScriptShowField("entry", userID, userID);
				}				
			}else{
				Employer emp = struct.getAppUser(userID);

				if (emp != null){
					field = new ScriptShowField("entry", userID, emp.getFullName());
				}else{
					field = new ScriptShowField("entry", userID, userID);
				}			
			}
			vals  += field.toString();
		}*/
		return "";
	}

}
