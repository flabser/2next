package cashtracker.test.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cashtracker.model.Category;
import cashtracker.model.constants.TransactionType;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CategoryTest extends Assert {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Category m = new Category();
		m.setId(1);
		m.setName("cat 1");
		m.setEnabled(true);
		m.setNote("cat note");

		List <TransactionType> transactionTypes = new ArrayList <TransactionType>();
		transactionTypes.add(TransactionType.EXPENSE);
		transactionTypes.add(TransactionType.INCOME);
		transactionTypes.add(TransactionType.TRANSFER);
		m.setTransactionTypes(transactionTypes);

		String json = mapper.writeValueAsString(m);
		Category c = mapper.readValue(json, Category.class);

		System.out.println(json);
		System.out.println(c);

		assertTrue(true);
	}
}
