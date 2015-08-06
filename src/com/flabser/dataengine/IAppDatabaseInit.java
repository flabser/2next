package com.flabser.dataengine;

import java.util.ArrayList;

import com.flabser.restful.Application;

public interface IAppDatabaseInit {
	void setApplicationProfile(Application application);

	ArrayList<String> getTablesDDE();

	ArrayList<String> getInitActions();
}
