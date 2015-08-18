package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.flabser.tests.InitEnv;


public class TagTest extends InitEnv {

	int iteration = 2;

	@Test
	public void test() {
		assertNotNull(db);

		TagDAO dao = new TagDAO(ses);

		int it = dao.findAll().size();

		for (int i = 0; i < iteration; i++) {
			Tag m = new Tag();
			m.setName("tag - " + it++);

			dao.add(m);
		}

		System.out.println(dao.findAll());
	}
}
