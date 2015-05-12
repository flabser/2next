package com.flabser.scriptprocessor;

import java.sql.Connection;
import java.util.Map;

import com.flabser.script._Document;
import com.flabser.script._Session;

public class ScriptSource implements IScriptSource{	
	private _Session session;
	private _Document document;
	private String lang;
//	private _DocumentCollection collection;
	private Map<String, String[]> formData;
	private Connection connection;

	public void setSession(_Session ses){			
		session = ses;
	}

	public void setDocument(_Document doc){
		document = doc;
	}

	@Override
	public void setLang(String lang) {
		this.lang = lang;		
	}
	
	@Override
	public void setFormData(Map<String, String[]> formData) {
		this.formData = formData;
	}

	@Override
	public void setConnection(Connection conn) {
		connection = conn;
	}
	
/*	@Override
	public void setDocumentCollection(_DocumentCollection col) {
		collection = col;
	}*/

	@Override
	public String providerHandlerProcess()throws Exception {
		return  doHandler(session, formData);
	}

	public String doHandler(_Session session, Map<String, String[]> formData){
		return "";
	}

	@Override
	public String patchHandlerProcess() throws Exception {
		return doHandler(session, connection);
	}
	

	public String doHandler(_Session session, Connection conn) {
		return "";
	}
	
	@Override
	public String schedulerHandlerProcess()throws Exception {
		return lang;
	//	return  doHandler(session, collection) ;
	}

	/*public String doHandler(_Session session, _DocumentCollection collection) throws Exception{
		return "";
	}*/

	public void setUser(String user){
	}

	public String[] simpleProcess(){
		return getStringValue();
	}

	public String[] getStringValue(){
		return getBlankValue();
	}

	public String[] documentProcess() {
		return getStringValue(document);
	}
	
	@Override
	public String[] documentLangProcess() {
		return getStringValue(document, lang);
	}


	public String[] getStringValue(_Document doc){		
		return getBlankValue();
	}
	
	public String[] getStringValue(_Document doc, String lang){		
		return getBlankValue();
	}

	public String[] sessionProcess() {	
		try{
			return getStringValue(session);
		}catch(Exception e){
			String[] result = {""};
			return result;
		}
	}

	@Override
	public String[] sessionLangProcess() {
		try{
			return getStringValue(session, lang);
		}catch(Exception e){
			String[] result = {""};
			return result;
		}
	}

	public String[] getStringValue(_Session session, String lang){
		return getBlankValue();
	}
	
	public String[] getStringValue(_Session session){
		return getBlankValue();
	}

	public static String[] getBlankValue(){
		String[] result = {""};
		return result;
	}

	@Override
	public String getConsoleOutput() {
		return null;
	}
	
	/*script  helper*/
	public String[] getAsArray(String val){
		String[] result = {val};
		return result;
	}


	


}
