package cashtracker.test.init;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

import cashtracker.init.FirstAction;


public class FirstActionTest extends Assert {

	@Test
	public void test() throws ClientProtocolException, IOException {

		FirstAction fa = new FirstAction();

		fa.getTablesDDE().forEach(System.out::println);
	}
}
