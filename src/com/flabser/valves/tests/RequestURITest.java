package com.flabser.valves.tests;

import org.junit.Assert;
import org.junit.Test;

import com.flabser.valves.RequestURL;

public class RequestURITest extends Assert {

	@Test
	public void test() {

		String v[] = { "/Nubis/Provider", "/Nubis", "/Nubis/", "/CashTracker", "/CashTracker//", "" };
		for (String url : v) {
			RequestURL ru = new RequestURL(url);
			assertNotNull(ru.getAppName());
			assertNotNull(ru.getAppID());
			assertEquals(ru.getAppID(), "");
			assertTrue(ru.isDefault());
		}

		String v1[] = { "/Nubis/info.html", "/Nubis/index.html", "/Nubis/css/info.css", "/CashTracker/js/gg.js", "/Nubis/logo.png", "/Nubis/index.htm",
				"/index.htm" };
		for (String url : v1) {
			RequestURL ru = new RequestURL(url);
			assertNotNull(ru.getAppName());
			assertNotNull(ru.getAppID());
			assertEquals(ru.getAppID(), "");
			assertTrue(ru.isSimpleObject());
		}

		String v2[] = { "/SharedResources/vendor/jquery/jquery-2.1.4.min.js", "/SharedResources", "/SharedResources/", "/SharedResources/jquery-2.1.4.min.js",
				"/SharedResources/img/logo.gif", "/SharedResources/index.htm" };
		for (String url : v2) {
			RequestURL ru = new RequestURL(url);
			assertTrue(ru.isSharedResource());
		}

		String v3[] = { "/Nubis/rest/session", "/CashTracker/rest/session" };
		for (String url : v3) {
			RequestURL ru = new RequestURL(url);
			assertNotNull(ru.getAppName());
			assertNotEquals(ru.getAppName(), "");
			assertTrue(ru.isAuthRequest());
		}

		String v4[] = { "/CashTracker/_zu2l161ce0cfi00b_/favicon.png", "/CashTracker/_zu2l161ce0cfi00b_/rest/session", "/CashTracker/zu2l161ce0cfi00b" };
		for (String url : v4) {
			RequestURL ru = new RequestURL(url);
			assertNotNull(ru.getAppName());
			assertNotEquals(ru.getAppName(), "");
			assertNotNull(ru.getAppID());
			assertNotEquals(ru.getAppID(), "");
			assertTrue(ru.isAuthRequest());
		}

		String v5[] = { "/CashTracker/Provider?id=login", "/CashTracker/Provider?type=page&id=login", "/CashTracker/rest/page/ws", "/Nubis/rest/page/welcome",
				"/CashTracker/_zu2l161ce0cfi00b_/Provider?id=login", "/CashTracker/Provider?type=page&id=login",
				"/CashTracker/_zu2l161ce0cfi00b_/rest/page/ws", "/Nubis/_zu2l161ce0cfi00b_/rest/page/welcome" };
		for (String url : v5) {
			RequestURL ru = new RequestURL(url);
			assertNotNull(ru.getAppName());
			assertNotEquals(ru.getAppName(), "");
			assertNotNull(ru.getAppID());
			assertTrue(ru.isPage());
		}

	}
}
