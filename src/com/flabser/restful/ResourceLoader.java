package com.flabser.restful;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.flabser.restful.admin.RestAdminProvider;
import com.flabser.solutions.cashtracker.services.AccountService;
import com.flabser.solutions.cashtracker.services.CategoryService;
import com.flabser.solutions.cashtracker.services.CostCenterService;
import com.flabser.solutions.cashtracker.services.TagService;
import com.flabser.solutions.cashtracker.services.TransactionService;


public class ResourceLoader extends Application {

	@Override
	public Set <Class <?>> getClasses() {
		final Set <Class <?>> classes = new HashSet <Class <?>>();

		// for in enabled_apps {
		// classes < rest_endpoint
		classes.add(TransactionService.class);
		classes.add(AccountService.class);
		classes.add(CategoryService.class);
		classes.add(CostCenterService.class);
		classes.add(TagService.class);
		// }

		// register root resource
		classes.add(Hello.class);
		classes.add(RestProvider.class);
		classes.add(RestAdminProvider.class);
		classes.add(SignIn.class);

		return classes;
	}
}
