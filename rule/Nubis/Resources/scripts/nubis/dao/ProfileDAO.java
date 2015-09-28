package nubis.dao;

import nubis.model.Profile;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class ProfileDAO extends DAO <Profile, Long> {

	public ProfileDAO(_Session session) {
		super(Profile.class, session);
	}
}
