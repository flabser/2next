package com.flabser.restful;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.flabser.restful.admin.UserService;
import com.flabser.restful.provider.ObjectMapperProvider;


public class ResourceLoader extends Application {
	private String appName;

	public ResourceLoader(String appName){
		super();
		this.appName = appName;
	}

	@Override
	public Set <Class <?>> getClasses() {
		final Set <Class <?>> classes = new HashSet <Class <?>>();

		classes.add(ObjectMapperProvider.class);

		classes.add(UserService.class);
		classes.add(SessionService.class);
		classes.add(RestProvider.class);

		if (!appName.equalsIgnoreCase("administrator")) {
			List<Class<?>> appClasses = ClassFinder.find(appName.toLowerCase() + ".rest");
			classes.addAll(appClasses);
		}

		return classes;
	}
}
