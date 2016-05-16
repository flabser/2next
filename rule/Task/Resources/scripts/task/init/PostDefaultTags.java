package task.init;

import java.util.ArrayList;
import java.util.List;

import task.model.Tag;
import cashtracker.dao.TagDAO;

import com.flabser.dataengine.deploying.IInitialData;
import com.flabser.localization.LanguageType;
import com.flabser.localization.Vocabulary;

public class PostDefaultTags implements IInitialData<Tag, TagDAO> {

	@Override
	public List<Tag> getData(LanguageType lang, Vocabulary vocabulary) {
		List<Tag> entities = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setName(vocabulary.getWord("favorite", lang));
		entities.add(t);
		return entities;
	}

	@Override
	public Class<TagDAO> getDAO() {
		return TagDAO.class;
	}

}
