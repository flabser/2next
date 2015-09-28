package cashtracker.test.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cashtracker.dao.TagDAO;
import cashtracker.model.Tag;
import cashtracker.validation.TagValidator;
import cashtracker.validation.ValidationError;

import com.flabser.dataengine.pool.DatabasePoolException;


public class TagTest extends InitEnv {

	TagDAO dao;
	private TagValidator validator = new TagValidator();

	@Before
	public void init() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			DatabasePoolException {
		super.init();
		dao = new TagDAO(ses);
	}

	@Test
	public void insertTest() {
		assertNotNull(db);

		int size = dao.findAll().size();
		int iteration = size + 50;

		for (int i = size; i < iteration; i++) {
			Tag m = new Tag();
			m.setName("tag - " + i);

			ValidationError ve = validator.validate(m);
			if (ve.hasError()) {
				for (cashtracker.validation.ValidationError.Error err : ve.getErrors()) {
					assertFalse("ValidationError : " + err.toString(), ve.hasError());
				}
			}

			dao.add(m);
		}
	}

	@Test
	public void selectTest() {
		List <Tag> list = dao.findAll();
		assertTrue(list.size() > 0);

		Tag mFirst = dao.findById(list.get(0).getId());
		Tag mLast = dao.findById(list.get(list.size() - 1).getId());

		System.out.println(mFirst);
		System.out.println(mLast);
		System.out.println(dao.getCount());

		assertNotNull(mFirst);
		assertNotNull(mLast);
	}

	@Test
	public void updateTest() {
		List <Tag> list = dao.findAll();

		for (Tag m : list) {
			m.setName(m.getName());
			System.out.println(dao.update(m));
		}
	}

	@Test
	public void deleteTest() {
		List <Tag> list = dao.findAll();

		for (Tag m : list) {
			if (!dao.existsTransactionByTag(m)) {
				dao.delete(m);
			}
		}
	}

	@Test
	public void existsByTagTest() {
		TagDAO tagDAO = new TagDAO(ses);
		Tag tag = (Tag) tagDAO.findAll().get(0);
		assertTrue(dao.existsTransactionByTag(tag));
	}
}
