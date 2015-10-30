package task.dao;

import task.model.Issue;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class IssueDAO extends DAO <Issue, Long> {

	public IssueDAO(_Session session) {
		super(Issue.class, session);
	}
}
