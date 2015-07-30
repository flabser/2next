package com.flabser.filters.tests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.flabser.filters.RequestURL;

public class RequestURITest extends Assert {

	@Test
	public void test() {
		ArrayList<String> variants = new ArrayList<String>();
		;
		variants.add("/Nubis/Provider");
		variants.add("/Nubis/css/app.css");
		variants.add("/SharedResources/vendor/jquery/jquery-2.1.4.min.js");
		variants.add("/Nubis/js/app.js");
		variants.add("/Nubis/rest/session");
		variants.add("/CashTracker/zu2l161ce0cfi00b/favicon.png");
		variants.add("/CashTracker/zu2l161ce0cfi00b/rest/session");
		variants.add("/CashTracker/zu2l161ce0cfi00b");
		variants.add("/CashTracker");
		variants.add("/CashTracker//");
		variants.add("");

		for (String val : variants) {
			System.out.println("val=" + val);
			RequestURL ru = new RequestURL(val);
			assertNotNull(ru);
			assertNotNull(ru.getAppName());
			assertNotNull(ru.getAppID());
		}

	}
}
