package cashtracker.test.rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TagServiceTest extends Assert {

	String url = "http://localhost:38779/CashTracker/-/rest/session";
	CloseableHttpClient client;

	@Before
	public void init() {
		client = HttpClientBuilder.create().build();
	}

	@After
	public void end() throws IOException {
		client.close();
	}

	@Test
	public void test() throws ClientProtocolException, IOException {

		HttpPost post = new HttpPost(url);

	}
}
