package com.flabser.tests.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.restful.AuthUser;
import com.flabser.users.User;
import com.flabser.util.Util;

public class UpdateUserProfileTest extends Assert {
	String url = "http://localhost:38779/Nubis/rest/session";
	CloseableHttpClient client = HttpClientBuilder.create().build();
	ISystemDatabase systemDatabase;

	@Before
	public void initBrowserFake() throws ClientProtocolException, IOException {
		systemDatabase = DatabaseFactory.getSysDatabase();
	}

	@Test
	public void test() throws ClientProtocolException, IOException {
		System.out.println("Authentication...");
		HttpPost post = new HttpPost(url);
		String ajson = "{\"authUser\":{\"login\":\"k-zone@ya.ru\",\"pwd\":\"galant387\"}}";
		StringEntity aentity = new StringEntity(ajson);
		post.setEntity(aentity);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		HttpResponse aresponse = client.execute(post);
		System.out.println("Result=" + aresponse.getStatusLine().getStatusCode());
		assertTrue(aresponse.getStatusLine().getStatusCode() == 200);

		System.out.println("Update UserProfile...");
		String login = Util.generateRandomAsText();
		String userName = Util.generateRandomAsText();
		String password = Util.generateRandomAsText();
		String email = Util.generateRandomAsText();
		String defaultApp = Util.generateRandomAsText();

		HttpPut put = new HttpPut(url);

		String json = "{\"authUser\":{\"login\":\"" + login + "\",\"pwd\":\"" + password + "\",\"name\":\"" + userName + "\",\"email\":\""
				+ email + "\",\"defaultApp\":\"" + defaultApp + "\"}}";
		StringEntity entity = new StringEntity(json);
		put.setEntity(entity);
		put.setHeader("Accept", "application/json");
		put.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(put);
		System.out.println("Result=" + response.getStatusLine().getStatusCode());
		assertTrue(response.getStatusLine().getStatusCode() == 200);

		System.out.println("Get current user...");
		// client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse getResponse = client.execute(get);
		System.out.println("Result=" + getResponse.getStatusLine().getStatusCode());
		assertTrue(getResponse.getStatusLine().getStatusCode() == 200);

		BufferedReader rd = new BufferedReader(new InputStreamReader(getResponse.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println("JSON Result= " + result);
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		AuthUser au = mapper.readValue(result.toString(), AuthUser.class);
		assertNotNull(au);

		assertEquals(au.getLogin(), login);
		assertEquals(au.getName(), userName);
		assertEquals(au.getPwd(), password);
		assertEquals(au.getEmail(), email);

		System.out.println("Check user in database...");

		User updatedUser = systemDatabase.getUser(au.getLogin());
		assertEquals(updatedUser.getLogin(), login);
		assertEquals(updatedUser.getUserName(), userName);
		assertEquals(updatedUser.getPwd(), password);
		assertEquals(updatedUser.getEmail(), email);

	}
}
