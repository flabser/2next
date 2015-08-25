package cashtracker.test.init;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.init.FirstAction;


public class FirstActionTest extends Assert {

	@Test
	public void test() {
		FirstAction fa = new FirstAction();
		fa.getTablesDDE().forEach(System.out::println);
	}
}
