package cashtracker.test.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;

import com.flabser.restful.data.IAppEntity;
import com.flabser.tests.InitEnv;


public class TagTest extends InitEnv {

	@Test
	public void test() {
		assertNotNull(db);

		TagDAO dao = new TagDAO(ses);

		int size = dao.findAll().size();
		int iteration = size + 1;

		for (int i = size; i < iteration; i++) {
			Tag m = new Tag();
			m.setName("tag - " + i);

			dao.add(m);
		}

		List <IAppEntity> tags = dao.findAll();
		for (IAppEntity itag : tags) {
			Tag tag = (Tag) itag;
			tag.getTransactions().forEach(System.out::println);
		}
	}
}
