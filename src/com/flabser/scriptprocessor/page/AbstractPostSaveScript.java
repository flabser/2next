package com.flabser.scriptprocessor.page;

import com.flabser.appenv.AppEnv;
import com.flabser.script._Document;
import com.flabser.script._Session;

@Deprecated
public abstract class AbstractPostSaveScript implements IPostSaveScript {	
	private _Session ses;
	private _Document doc;
	private String user;
	
	public void setSession(_Session ses){			
		this.ses = ses;
	}
		
	public void setDocument(_Document doc){
		this.doc = doc;
	}
	
	@Deprecated
	public void setUser(String user){
		this.user = user;
	}
	
	public void setAppEnv(AppEnv env){
		
	}
	
	public void process(){
		try{
			doPostSave(ses, doc, user);		
		}catch(Exception e){
		
		}	
	}
	
	
		
	public abstract void doPostSave(_Session ses, _Document doc, String user);
	
	
}
