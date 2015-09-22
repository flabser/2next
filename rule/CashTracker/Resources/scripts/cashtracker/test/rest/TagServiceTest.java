package cashtracker.test.rest;

import org.junit.Assert;
import org.junit.Test;


public class TagServiceTest extends Assert {

	/*
	 * String url = "http://localhost:38779/CashTracker/-/rest/session";
	 * CloseableHttpClient client;
	 * 
	 * @Before public void init() { client = HttpClientBuilder.create().build();
	 * }
	 * 
	 * @After public void end() throws IOException { client.close(); }
	 * 
	 * @Test public void test() throws ClientProtocolException, IOException {
	 * 
	 * HttpPost post = new HttpPost(url);
	 * 
	 * }
	 */

	@Test
	public void test() {
		int count = 14999;
		int limit = 60;
		int offset = 540;

		int page = count / limit;

		System.out.println("page=" + calculatePageCount(count, limit));
		System.out.println("prev:" + (offset - limit) + ", next:" + (offset + limit));

	}

	public static int calculatePageCount(int count, int limit) {
		return (count > limit) ? (int) Math.ceil((double) count / limit) : 1;
	}
}
