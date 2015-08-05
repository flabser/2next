package com.flabser.dataengine;

import java.util.ArrayList;
import java.util.Map;

import com.flabser.dataengine.system.entities.ApplicationProfile;

public interface IAppDatabaseInit {
	void setApplicationProfile(ApplicationProfile ap);

	Map<String, String> getTablesDDE();

	ArrayList<String> getInitActions();
}
