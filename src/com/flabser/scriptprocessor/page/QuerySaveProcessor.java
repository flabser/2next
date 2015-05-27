package com.flabser.scriptprocessor.page;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.control.CompilerConfiguration;
import com.flabser.env.Environment;
import com.flabser.localization.Vocabulary;
import com.flabser.rule.constants.RunMode;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.script._WebFormData;
import com.flabser.scriptprocessor.ScriptProcessorUtil;
import java.io.File;
import java.util.ArrayList;


public class QuerySaveProcessor {
	public ArrayList<IQuerySaveTransaction> transactionToPost = new ArrayList<IQuerySaveTransaction>();
	private GroovyObject groovyObject = null;
	private _Session ses;
	private _Document doc;
	private Vocabulary vocabulary;
	private String currentLang;
	private _WebFormData webFormData;
	
	

	public QuerySaveProcessor(){

	}

	

	public QuerySaveResult processScript(String className) throws ClassNotFoundException {		
		try {	
			Class querySaveClass = null;
			ClassLoader parent = this.getClass().getClassLoader();
			CompilerConfiguration compiler = new CompilerConfiguration();
			if (Environment.debugMode == RunMode.ON){		
				compiler.setRecompileGroovySource(true);
				compiler.setMinimumRecompilationInterval(0);
				File curDir = new File("libs");
				File bin = new File("bin");
				String sd = curDir.getAbsolutePath();
				compiler.setTargetDirectory(bin.getAbsolutePath());
				compiler.setClasspath(sd);
//				compiler.setClasspath("C:\\workspace\\NextBase\\bin\\");
				GroovyClassLoader classLoader = new GroovyClassLoader(parent, compiler);
				classLoader.setShouldRecompile(true);
			 	querySaveClass = Class.forName(className, true, classLoader);
			}else{
				querySaveClass = Class.forName(className);
			}
		
			groovyObject = (GroovyObject) querySaveClass.newInstance();
		} catch (InstantiationException e) {					
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IQuerySaveScript myObject = (IQuerySaveScript) groovyObject;

		myObject.setSession(ses);
		myObject.setDocument(doc);
		myObject.setWebFormData(webFormData);	
		myObject.setCurrentLang(vocabulary, currentLang);
		return myObject.process();		
	}

	
}
