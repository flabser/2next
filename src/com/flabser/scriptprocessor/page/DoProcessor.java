package com.flabser.scriptprocessor.page;

import groovy.lang.GroovyObject;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.flabser.appenv.AppEnv;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script._WebFormDataRest;
import com.flabser.users.UserSession;
import com.flabser.util.ScriptResponse;

public class DoProcessor {

	public ArrayList<IQuerySaveTransaction> transactionToPost = new ArrayList<IQuerySaveTransaction>();

	private String lang;
	private _Session ses;
	private Vocabulary vocabulary;
	private _WebFormData webFormData;

	public DoProcessor(AppEnv env, UserSession u, String currentLang, Map<String, String[]> formData) {
		ses = new _Session(env, u);
		vocabulary = env.vocabulary;
		lang = currentLang;
		if (formData instanceof MultivaluedMap) {
			webFormData = new _WebFormDataRest((MultivaluedMap) formData);
		} else {
			webFormData = new _WebFormData(formData);
		}
	}

	public ScriptResponse processGroovyScript(String className, String method) throws ClassNotFoundException {
		GroovyObject groovyObject = null;
		try {
			Class<?> pageClass = Class.forName(className);
			groovyObject = (GroovyObject) pageClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IPageScript myObject = (IPageScript) groovyObject;

		myObject.setSession(ses);
		myObject.setFormData(webFormData);
		myObject.setMethod(method);
		myObject.setCurrentLang(vocabulary, lang);

		return myObject.process();
	}

	public ScriptResponse processJava(String className, String method) throws ClassNotFoundException {
		Object object = null;
		try {
			Class<?> pageClass = Class.forName(className);
			object = pageClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IPageScript myObject = (IPageScript) object;

		myObject.setSession(ses);
		myObject.setFormData(webFormData);
		myObject.setMethod(method);
		myObject.setCurrentLang(vocabulary, lang);

		return myObject.process();
	}
}
