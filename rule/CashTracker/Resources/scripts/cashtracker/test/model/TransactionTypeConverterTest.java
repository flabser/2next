package cashtracker.test.model;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.TransactionTypesConverter;


public class TransactionTypeConverterTest extends Assert {

	@Test
	public void test() throws Exception {
		TransactionTypesConverter tpc = new TransactionTypesConverter();

		List <TransactionType> tt = tpc.convertToEntityAttribute(new String[] { "E", "I", "T" });
		System.out.println(tt);

		String[] ts = tpc.convertToDatabaseColumn(tt);
		System.out.println(ts[0]);
		System.out.println(ts[1]);
		System.out.println(ts[2]);
	}
}
