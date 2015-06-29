package com.flabser.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import cashtracker.rest.AccountService;
import cashtracker.rest.CategoryService;
import cashtracker.rest.CostCenterService;
import cashtracker.rest.TagService;
import cashtracker.rest.TransactionService;

import com.flabser.restful.admin.UserService;
import com.flabser.restful.provider.ObjectMapperProvider;


public class ResourceLoader extends Application {

	@Override
	public Set <Class <?>> getClasses() {
		final Set <Class <?>> classes = new HashSet <Class <?>>();
		classes.add(ObjectMapperProvider.class);

		classes.add(UserService.class);
		classes.add(SessionService.class);
		classes.add(RestProvider.class);

		classes.add(TransactionService.class);
		classes.add(AccountService.class);
		classes.add(CategoryService.class);
		classes.add(CostCenterService.class);
		classes.add(TagService.class);

		return classes;
	}
}
