package com.flabser.filters.tests;

import org.junit.Assert;
import org.junit.Test;

import com.flabser.filters.RequestURL;

public class RequestURITest extends Assert {

	@Test
	public void test() {

		assertNotNull(new RequestURL("/Nubis/Provider").getAppName());
		assertNotNull(new RequestURL("/Nubis/Provider").getAppID());
		assertNotNull(new RequestURL("/Nubis/css/app.css").getAppName());
		assertNotNull(new RequestURL("/Nubis/css/app.css").getAppID());
		assertNotNull(new RequestURL("/SharedResources/vendor/jquery/jquery-2.1.4.min.js").getAppName());
		assertNotNull(new RequestURL("/SharedResources/vendor/jquery/jquery-2.1.4.min.js").getAppID());
		assertNotNull(new RequestURL("/Nubis/js/app.js").getAppName());
		assertNotNull(new RequestURL("/Nubis/js/app.js").getAppID());
		assertNotNull(new RequestURL("/Nubis/rest/session").getAppName());
		assertNotNull(new RequestURL("/Nubis/rest/session").getAppID());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b/favicon.png").getAppName());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b/favicon.png").getAppID());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b/rest/session").getAppName());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b/rest/session").getAppID());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b").getAppName());
		assertNotNull(new RequestURL("/CashTracker/zu2l161ce0cfi00b").getAppID());
		assertNotNull(new RequestURL("/CashTracker").getAppName());
		assertNotNull(new RequestURL("/CashTracker").getAppID());
		assertNotNull(new RequestURL("/CashTracker//").getAppName());
		assertNotNull(new RequestURL("/CashTracker//").getAppID());
		assertNotNull(new RequestURL("").getAppName());
		assertNotNull(new RequestURL("").getAppID());

	}
}
