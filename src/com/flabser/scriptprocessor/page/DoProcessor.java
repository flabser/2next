package com.flabser.scriptprocessor.page;

import groovy.lang.GroovyObject;

import java.util.ArrayList;
import java.util.Map;

import com.flabser.appenv.AppEnv;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.util.XMLResponse;

public class DoProcessor {
	public ArrayList<IQuerySaveTransaction> transactionToPost = new ArrayList<IQuerySaveTransaction>();
	
	private String lang;
	private GroovyObject groovyObject = null;
	private _Session ses;
	private Vocabulary vocabulary;
	private _WebFormData webFormData;	

	
	public DoProcessor(AppEnv env, UserSession u, String currentLang, Map<String, String[]> formData){
		ses = new _Session(env, u);	
	//	ses.getCurrentDatabase().setTransConveyor(transactionToPost);
		vocabulary = env.vocabulary;
		lang = currentLang;
		webFormData = new _WebFormData(formData);		
	}


	public XMLResponse processScript(String className) throws ClassNotFoundException {		
		try {		
			Class pageClass = Class.forName(className);		
			groovyObject = (GroovyObject) pageClass.newInstance();
		} catch (InstantiationException e) {					
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IPageScript myObject = (IPageScript) groovyObject;

		myObject.setSession(ses);
		myObject.setFormData(webFormData);	
		myObject.setCurrentLang(vocabulary, lang);
		
		return myObject.process();
	
		
	}

	
}
