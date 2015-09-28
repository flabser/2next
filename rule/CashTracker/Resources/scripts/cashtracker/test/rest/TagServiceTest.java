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
	public void testContains() {
		/*List <String> l1 = new ArrayList <String>();
		List <String> l2 = new ArrayList <String>();

		l1.add("1");
		l1.add("2");

		l2.add("1");
		l2.add("2");
		// l2.add("3");

		assertTrue("l1.contains(l2)", l1.containsAll(l2));*/

		System.out.println(cachePatternMatch("ssddf/sdew/ghjg/assets/app.js"));
	}

	// @Test
	public void test() {
		int count = 14999;
		int limit = 60;
		int offset = 540;

		int page = count / limit;

		System.out.println("page=" + calculatePageCount(count, limit));
		System.out.println("prev:" + (offset - limit) + ", next:" + (offset + limit));
	}

	private static boolean cachePatternMatch(String requestUri) {
		String[] pattern = new String[] { ".ico", ".jpg", ".jpeg", ".png", ".gif", ".js", ".css" };

		for (String p : pattern) {
			if (requestUri.endsWith(p)) {
				return true;
			}
		}

		return false;
	}

	public static int calculatePageCount(int count, int limit) {
		return (count > limit) ? (int) Math.ceil((double) count / limit) : 1;
	}
}
