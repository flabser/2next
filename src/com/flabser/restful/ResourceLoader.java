package com.flabser.restful;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.flabser.restful.admin.AppService;
import com.flabser.restful.admin.InvService;
import com.flabser.restful.admin.LogService;
import com.flabser.restful.admin.UserService;
import com.flabser.restful.provider.ObjectMapperProvider;
import com.flabser.server.Server;

public class ResourceLoader extends Application {
	private String appName;

	public ResourceLoader(String appName) {
		super();
		Server.logger.normalLogEntry("6576");
		this.appName = appName;
	}

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();

		classes.add(ObjectMapperProvider.class);
		classes.add(ApplicationService.class);
		classes.add(UserService.class);
		classes.add(InvService.class);
		classes.add(SessionService.class);
		classes.add(RestProvider.class);
		classes.add(MultiPartFeature.class);
		classes.add(UploadFileService.class);
		classes.add(WorkspaceService.class);
		Server.logger.normalLogEntry("85492");
		if (!appName.equalsIgnoreCase("administrator")) {
			Server.logger.normalLogEntry("6f576");
			List<Class<?>> appClasses = ClassFinder.find(appName.toLowerCase() + ".rest");
			classes.addAll(appClasses);
		} else {
			classes.add(AppService.class);
			classes.add(LogService.class);
		}

		return classes;
	}
}
