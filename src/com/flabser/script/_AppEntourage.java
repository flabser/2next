package com.flabser.script;

import java.util.ArrayList;
import com.flabser.appenv.AppEnv;
import com.flabser.server.Server;


public class _AppEntourage {
	private AppEnv env;

	public _AppEntourage(_Session ses, AppEnv env) {
		this.env = env;
	}

	public String getServerVersion(){
		return Server.serverVersion;
	}

	public String getBuildTime(){
		return Server.compilationTime;
	}
	
	
	public String getAppName(){
		return env.appType;
	}

	public ArrayList<_Element> getAvailableLangs() throws _Exception{
		return null;
		/*ViewEntryCollection vec = new ViewEntryCollection(ses, 100);


		for(Lang l: env.globalSetting.langsList){
			String p[] = {l.isOn.toString(),l.id,l.name,Boolean.toString(l.isPrimary)};
			try {
				IViewEntry entry = new ViewEntry(p);
				vec.add(entry);
			} catch (SQLException e) {
				throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, "internal error: function: _Document.getAvailableLangs()");
			}
		}
		return vec.getScriptingObj();*/
	}
	
	public ArrayList<_Element>  getAvailableApps() throws _Exception{
		return null;
		/*ViewEntryCollection vec = new ViewEntryCollection(ses, 100);
		_Employer emp = ses.getCurrentAppUser();
		
		for(AppEnv appEnv: Environment.getApplications()){
			if (appEnv.isValid && !appEnv.globalSetting.isWorkspace){					
				if (emp.isAuthorized()){
					Collection<UserApplicationProfile> enabledApps;
					try {
						enabledApps = emp.getEnabledApps();
					} catch (DocumentException e1) {
						throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, "internal error: function: _Document.getAvailableApps()");
					}	
					for(UserApplicationProfile uap: enabledApps){
						if(uap.appName.equals(appEnv.appType)){	

							String p[] = {appEnv.appType, appEnv.globalSetting.defaultRedirectURL, appEnv.globalSetting.logo, appEnv.globalSetting.orgName, appEnv.globalSetting.description};
							try {
								IViewEntry entry = new ViewEntry(p);
								vec.add(entry);
							} catch (SQLException e) {
								throw new _Exception(_ExceptionType.SCRIPT_ENGINE_ERROR, "internal error: function: _Document.getAvailableApps()");
							}

						}
					}
				}
			}*/
		}

	
}
