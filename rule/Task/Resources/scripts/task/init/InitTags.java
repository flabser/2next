package task.init;

import java.util.ArrayList;
import java.util.List;

import com.flabser.dataengine.deploing.IInitialData;
import com.flabser.dataengine.jpa.ISimpleAppEntity;

import cashtracker.dao.TagDAO;
import task.model.Tag;

public class InitTags implements IInitialData {

	@Override
	public List<ISimpleAppEntity> getData() {
		ArrayList<ISimpleAppEntity> entities = new ArrayList<ISimpleAppEntity>();
		Tag t = new Tag();
		t.setName("favourite");
		entities.add(t);
		return entities;
	}

	@Override
	public Class<?> getDAO() {
		return TagDAO.class;
	}

}
