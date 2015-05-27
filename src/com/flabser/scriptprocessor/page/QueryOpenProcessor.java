package com.flabser.scriptprocessor.page;

import java.util.HashMap;
import org.codehaus.groovy.control.CompilerConfiguration;
import com.flabser.appenv.AppEnv;
import com.flabser.env.Environment;
import com.flabser.localization.Vocabulary;
import com.flabser.rule.constants.RunMode;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.users.User;

import groovy.lang.*;

public class QueryOpenProcessor{
	private GroovyObject groovyObject = null;
	private _Session ses;
	private _Document doc;
	private Vocabulary vocabulary;
	private String currentLang;
	private _WebFormData webFormData;
	private AppEnv env;
	
	public QueryOpenProcessor(AppEnv env, User u, String currentLang, HashMap<String, String[]> formData){
		this.env = env;
		vocabulary = env.vocabulary;
		this.currentLang = currentLang;
		webFormData = new _WebFormData(formData);
	}
	
	

	public PublishResult processScript(String className) throws ClassNotFoundException {		
		try {	
			Class queryClass = null;
			ClassLoader parent = this.getClass().getClassLoader();
			CompilerConfiguration compiler = new CompilerConfiguration();
			if (Environment.debugMode == RunMode.ON){				
				compiler.setRecompileGroovySource(true);
				compiler.setMinimumRecompilationInterval(0);
				GroovyClassLoader classLoader = new GroovyClassLoader(parent, compiler);
				classLoader.setShouldRecompile(true);
				queryClass = Class.forName(className, true, classLoader);
			}else{
				queryClass = Class.forName(className);
			}
			groovyObject = (GroovyObject) queryClass.newInstance();
		} catch (InstantiationException e) {					
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IQueryOpenScript myObject = (IQueryOpenScript) groovyObject;

		myObject.setAppEnv(env);
		myObject.setSession(ses);
		myObject.setFormData(webFormData);	
		myObject.setCurrentLang(vocabulary, currentLang);
		if (doc != null){
			myObject.setDocument(doc);
			return myObject.process2();
		}else{		
			return myObject.process1();
		}
	}
	
}
