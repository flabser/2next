package com.flabser.tests.user;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;

public class GetCurrentUserTest extends Assert {
	String url = "http://localhost:38779/Nubis/rest/session";
	CloseableHttpClient client;
	BasicCookieStore cookieStore;
	HttpContext localContext;

	ISystemDatabase systemDatabase;

	@Before
	public void initBrowserFake() throws ClientProtocolException, IOException {
		systemDatabase = DatabaseFactory.getSysDatabase();
		cookieStore = new BasicCookieStore();
		localContext = new BasicHttpContext();
		client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}

	@Test
	public void test() throws ClientProtocolException, IOException {
		System.out.println("Authentication...");
		String ajson = "{\"authUser\":{\"login\":\"k-zone@ya.ru\",\"pwd\":\"galant387\"}}";
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client.target("http://localhost:38779/Nubis/rest");
		WebTarget resourceWebTarget = webTarget.path("session");
		Builder builder = resourceWebTarget.request(MediaType.APPLICATION_JSON);
		builder.accept(MediaType.APPLICATION_JSON);
		Response bean = builder.post(Entity.entity(ajson, MediaType.APPLICATION_JSON), Response.class);
		System.out.println(bean);
		Response getResp = builder.get(Response.class);
		System.out.println(getResp);
		System.out.println(getResp.readEntity(String.class));

	}
}
