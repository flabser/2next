package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.dataengine.system.entities.ApplicationProfile;

public interface IAppDatabaseInit {
	void setApplicationProfile(ApplicationProfile ap);
    
    ArrayList <String> getTablesDDE();
    
    ArrayList<String> getInitActions();
}
