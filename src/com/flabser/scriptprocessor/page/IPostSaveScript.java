package com.flabser.scriptprocessor.page;

import com.flabser.appenv.AppEnv;
import com.flabser.script._Document;
import com.flabser.script._Session;

public interface IPostSaveScript {	
	void setSession(_Session ses);	
	void setDocument(_Document doc);
	void setAppEnv(AppEnv env);	
	void process();
}
