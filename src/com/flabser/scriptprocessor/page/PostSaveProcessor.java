package com.flabser.scriptprocessor.page;

import groovy.lang.GroovyObject;

import java.util.ArrayList;

import com.flabser.appenv.AppEnv;
import com.flabser.script._Document;
import com.flabser.script._Session;
import com.flabser.scriptprocessor.ScriptProcessorUtil;

public class PostSaveProcessor extends Thread {
	public ArrayList<IQuerySaveTransaction> transactionToPost = new ArrayList<IQuerySaveTransaction>();
	
	private Class<GroovyObject> scriptClass;	
	private GroovyObject groovyObject = null;
	private _Session ses;
	private _Document doc;
	private String user;	
	private AppEnv env;
	
	/*public PostSaveProcessor(BaseDocument d, User u){
		env = d.getAppEnv();
		IDatabase db = DatabaseFactory.getDatabase(env.appType);
		ses = new _Session(db.getParent(), u, this);
		ses.getCurrentDatabase().setTransConveyor(transactionToPost);
		if (d instanceof Project){			
			doc = new _Project((Project)d, ses);
		}else if (d instanceof Task){
			doc = new _Task((Task)d, ses);
		}else if (d instanceof Execution){
			doc = new _Execution((Execution)d, ses);
		}else{
			doc = new _Document(d, ses);
		}	
		user = u.getUserID();		
	}*/
	
	

	public void run() {		
		
		try {				
			groovyObject = scriptClass.newInstance();
		} catch (InstantiationException e) {					
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		IPostSaveScript myObject = (IPostSaveScript) groovyObject;
		
		myObject.setSession(ses);
		myObject.setDocument(doc);
	//	myObject.setUser(user);		
		myObject.setAppEnv(env);	
		myObject.process();
	}
	
	public void setClass(Class<GroovyObject> postSaveClass) {
		this.scriptClass = postSaveClass;		
	}
	
	@SuppressWarnings("unchecked")
	public void setClass(String className) throws ClassNotFoundException {
		Class postSaveClass = Class.forName(className);
		this.scriptClass = postSaveClass;		
	}
	
	public static String normalizeScript(String script) {
		String beforeScript = 
				"import java.util.HashSet;" +
				"import kz.flabs.dataengine.Const;" +
			    "import kz.flabs.scriptprocessor.form.postsave.*;" +  
				ScriptProcessorUtil.packageList +
				"class Foo extends AbstractPostSaveScript{";
			String afterScript = "}";
			return beforeScript + script + afterScript;		
	}

	
}
