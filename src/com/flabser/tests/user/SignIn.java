package com.flabser.tests.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flabser.restful.AuthUser;

public class SignIn extends Assert {
	String url = "http://localhost:38779/Nubis/rest/session";
	CloseableHttpClient client;

	@Before
	public void initBrowserFake() {
		client = HttpClientBuilder.create().build();

	}

	@Test
	public void test() throws ClientProtocolException, IOException {

		HttpPost post = new HttpPost(url);

		String json = "{\"authUser\":{\"login\":\"k-zone@ya.ru\",\"pwd\":\"galant387\"}}";
		StringEntity entity = new StringEntity(json);
		post.setEntity(entity);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		HttpResponse response = client.execute(post);
		System.out.println(response.getStatusLine().getStatusCode());
		assertTrue(response.getStatusLine().getStatusCode() == 200);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result);
		// ObjectMapper mapper = new ObjectMapper();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		AuthUser user = mapper.readValue(result.toString(), AuthUser.class);
		System.out.println(user);

	}

}
