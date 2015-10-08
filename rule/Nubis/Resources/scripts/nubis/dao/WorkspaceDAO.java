package nubis.dao;

import nubis.model.Profile;
import nubis.model.Workspace;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class WorkspaceDAO extends DAO <Profile, Long> {

	public WorkspaceDAO(_Session session) {
		super(Profile.class, session);
	}


	public Workspace find() {
		// TODO Auto-generated method stub
		return null;
	}
}
