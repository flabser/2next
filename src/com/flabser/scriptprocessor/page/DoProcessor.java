package com.flabser.scriptprocessor.page;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.flabser.apptemplate.AppTemplate;
import com.flabser.exception.WebFormValueException;
import com.flabser.localization.Vocabulary;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.script._WebFormDataRest;
import com.flabser.servlets.SessionCooks;
import com.flabser.util.ScriptResponse;

import groovy.lang.GroovyObject;

public class DoProcessor {

	public ArrayList<IQuerySaveTransaction> transactionToPost = new ArrayList<IQuerySaveTransaction>();

	private String lang;
	private _Session ses;
	private Vocabulary vocabulary;
	private _WebFormData webFormData;
	private SessionCooks cooks;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DoProcessor(AppTemplate env, _Session ses, Map<String, String[]> formData, String context,
			SessionCooks cooks) {
		vocabulary = env.vocabulary;
		this.ses = ses;
		lang = ses.getLang();
		if (formData instanceof MultivaluedMap) {
			webFormData = new _WebFormDataRest((MultivaluedMap) formData);
		} else {
			webFormData = new _WebFormData(formData);
		}
		this.cooks = cooks;
	}

	public ScriptResponse processGroovyScript(String className, String method)
			throws ClassNotFoundException, WebFormValueException {
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
		myObject.setCooks(cooks);

		return myObject.process();
	}

	public ScriptResponse processJava(String className, String method)
			throws ClassNotFoundException, WebFormValueException {
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
		myObject.setCooks(cooks);

		return myObject.process();
	}
}
