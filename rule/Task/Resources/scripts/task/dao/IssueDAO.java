package task.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;

import task.dao.filter.IssueFilter;
import task.helper.PageRequest;
import task.model.Issue;


public class IssueDAO extends DAO <Issue, Long> {

	private final static String SELECT_ALL = "SELECT m FROM Issue AS m";

	public IssueDAO(_Session session) {
		super(Issue.class, session);
	}

	public List <Issue> find(IssueFilter filter, PageRequest pageRequest) {
		String tags = "";
		String assignees = "";
		String status = "";
		String dueDateFrom = "";
		String dueDateUntil = "";
		boolean hasWhere = false;

		if (filter.getStatus() != null) {
			status = " m.status = :status";
			hasWhere = true;
		}
		if (!filter.getTags().isEmpty()) {
			tags = (hasWhere ? " AND " : "") + " m.tags IN :tags";
			hasWhere = true;
		}
		if (!filter.getAssignees().isEmpty()) {
			assignees = (hasWhere ? " AND " : "") + " m.assignee IN :assignees";
			hasWhere = true;
		}
		if (filter.getDueDateFrom() != null) {
			dueDateFrom = (hasWhere ? " AND " : "") + " m.dueDate > :sdate";
			hasWhere = true;
		}
		if (filter.getDueDateUntil() != null) {
			dueDateUntil = (hasWhere ? " AND " : "") + " m.dueDate < :edate";
			hasWhere = true;
		}

		String jpql = SELECT_ALL + (hasWhere ? " WHERE " : "") + status + tags + assignees + dueDateFrom + dueDateUntil
				+ " ORDER BY m.dueDate ASC";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery <Issue> q = em.createQuery(jpql, Issue.class);

			if (filter.getStatus() != null) {
				q.setParameter("status", filter.getStatus());
			}
			if (!tags.isEmpty()) {
				q.setParameter("tags", filter.getTags());
			}
			if (!assignees.isEmpty()) {
				q.setParameter("assignees", filter.getAssignees());
			}
			if (!dueDateFrom.isEmpty()) {
				q.setParameter("sdate", filter.getDueDateFrom());
			}
			if (!dueDateUntil.isEmpty()) {
				q.setParameter("edate", filter.getDueDateUntil());
			}

			q.setFirstResult(pageRequest.getOffset());
			q.setMaxResults(pageRequest.getLimit());

			return q.getResultList();
		} finally {
			em.close();
		}
	}
}
