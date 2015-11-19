package task.init;

import java.util.ArrayList;
import java.util.List;

import com.flabser.dataengine.deploing.IInitialData;

import cashtracker.dao.TagDAO;
import task.model.Tag;

public class InitTags implements IInitialData<Tag, TagDAO> {

	@Override
	public List<Tag> getData() {
		List<Tag> entities = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setName("favourite");
		entities.add(t);
		return entities;
	}

	@Override
	public Class<TagDAO> getDAO() {
		return TagDAO.class;
	}

}
