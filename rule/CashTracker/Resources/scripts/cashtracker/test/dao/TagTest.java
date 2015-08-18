package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.flabser.tests.InitEnv;


public class TagTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		TagDAO dao = new TagDAO(ses);

		int size = dao.findAll().size();
		int iteration = size + 2;

		for (int i = size; i < iteration; i++) {
			Tag m = new Tag();
			m.setName("tag - " + i);

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
