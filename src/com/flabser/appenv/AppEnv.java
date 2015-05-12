package com.flabser.appenv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.flabser.dataengine.Const;
import com.flabser.exception.RuleException;
import com.flabser.localization.Localizator;
import com.flabser.localization.LocalizatorException;
import com.flabser.localization.Vocabulary;
import com.flabser.rule.GlobalSetting;
import com.flabser.rule.Role;
import com.flabser.rule.RuleProvider;
import com.flabser.rule.constants.RunMode;
import com.flabser.runtimeobj.caching.ICache;
import com.flabser.runtimeobj.page.Page;

public class AppEnv implements Const, ICache{ 
	public boolean isValid;
	public String appType = "undefined";	
	public RuleProvider ruleProvider;
	public HashMap<String, File> xsltFileMap = new HashMap<String, File>();
	public String adminXSLTPath;	
	public static com.flabser.log.ILogger logger;	
	public GlobalSetting globalSetting;
	public Vocabulary vocabulary;

	private HashMap<String, StringBuffer> cache = new HashMap<String, StringBuffer>();

	public AppEnv(String at){
		isValid = true;
		appType = "administrator";
	}


	public AppEnv(String appType, String globalFileName){
		this.appType = appType;
		try{			
			AppEnv.logger.normalLogEntry("# Start application \"" + appType + "\"");
			ruleProvider = new RuleProvider(this);		
			ruleProvider.initApp(globalFileName);
			globalSetting = ruleProvider.global;			
			
			if (globalSetting.isOn == RunMode.ON){				
				if (globalSetting.langsList.size() > 0){
					AppEnv.logger.normalLogEntry("Dictionary is loading...");

					try{
						Localizator l = new Localizator(globalSetting);
						vocabulary = l.populate("vocabulary");
						if (vocabulary != null){
							AppEnv.logger.normalLogEntry("Dictionary has loaded");
						}							
					}catch(LocalizatorException le){
						AppEnv.logger.verboseLogEntry(le.getMessage());
					}

				}

				isValid = true;
			}else{
				AppEnv.logger.warningLogEntry("Application: \"" + appType + "\" is off");
			
			}

		}catch(Exception e) {
			AppEnv.logger.errorLogEntry(e);	
		}
	}

		public ArrayList<Role> getRolesList(){
		ArrayList <Role> rolesList = (ArrayList<Role>) globalSetting.roleCollection.getRolesList().clone();
		
		return rolesList;
	}
	

	
	public HashMap<String, Role> getRolesMap(){
		HashMap<String, Role> rolesMap = (HashMap<String, Role>) globalSetting.roleCollection.getRolesMap().clone();
		
		return rolesMap;
	}

	public String toString(){
		return appType + "(" + globalSetting.implemantion + ")";
	}

	

	@Override
	public StringBuffer getPage(Page page, Map<String, String[]> formData) throws ClassNotFoundException, RuleException {
		boolean reload = false;
		Object obj = cache.get(page.getID());	
		String p[] = formData.get("cache");
		if (p != null){
			String cacheParam = formData.get("cache")[0];	
			if (cacheParam.equalsIgnoreCase("reload")){
				reload = true;
			}
		}		
		if (obj == null || reload){
			StringBuffer buffer = page.getContent(formData);
			cache.put(page.getID(),buffer);
			return buffer;
		}else{
			return (StringBuffer)obj;	
		}

	}


	@Override
	public void flush() {
		cache.clear();
	}


	public static String getName() {
		return "appType";
	}

	

}
