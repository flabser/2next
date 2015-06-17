package com.flabser.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.flabser.restful.admin.RestAdminProvider;

public class ResourceLoader extends Application {
	  @Override
	    public Set<Class<?>> getClasses() {
	        final Set<Class<?>> classes = new HashSet<Class<?>>();
	        
	        // register root resource
	        classes.add(Hello.class);
	        classes.add(RestProvider.class);
	        classes.add(RestAdminProvider.class);
	        classes.add(SignffffIn.class);
	        return classes;
	    }
}
