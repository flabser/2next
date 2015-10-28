package benexus.dao;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import benexus.model.Profile;

public class ProfileDAO extends DAO<Profile, Long> {

	public ProfileDAO(_Session session) {
		super(Profile.class, session);
	}
}
