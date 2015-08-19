package cashtracker.test.model;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.converter.TransactionStateConverter;


public class TransactionStateConverterTest extends Assert {

	@Test
	public void test() throws Exception {
		TransactionStateConverter tpc = new TransactionStateConverter();

		TransactionState tt = tpc.convertToEntityAttribute("C");
		System.out.println(tt);

		String ts = tpc.convertToDatabaseColumn(tt);
		System.out.println(ts);
	}
}
