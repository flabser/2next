package cashtracker.test.model.constants.converter;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionTypeConverter;


public class TransactionTypeConverterTest extends Assert {

	@Test
	public void test() throws Exception {
		TransactionTypeConverter tpc = new TransactionTypeConverter();

		TransactionType tt = tpc.convertToEntityAttribute("E");
		System.out.println(tt);

		String ts = tpc.convertToDatabaseColumn(tt);
		System.out.println(ts);
	}
}
