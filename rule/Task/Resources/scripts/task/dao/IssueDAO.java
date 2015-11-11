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
		String dateRange = "";
		String status = "";
		boolean hasWhere = false;

		if (filter.getStatus() != null) {
			status = " m.status = :status";
			hasWhere = true;
		}

		if (!filter.getTags().isEmpty()) {
			tags = (hasWhere ? " AND " : "") + " m.tags IN :tags";
			hasWhere = true;
		}

		if (filter.getMilestoneDateRange()[0] != null) {
			dateRange = (hasWhere ? " AND " : "") + " m.milestone > :sdate AND m.milestone < :edate";
			hasWhere = true;
		}

		String jpql = SELECT_ALL + (hasWhere ? " WHERE " : "") + status + tags + dateRange
				+ " ORDER BY m.milestone ASC";

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery <Issue> q = em.createQuery(jpql, Issue.class);

			if (filter.getStatus() != null) {
				q.setParameter("status", filter.getStatus());
			}
			if (!tags.isEmpty()) {
				q.setParameter("tags", filter.getTags());
			}
			if (!dateRange.isEmpty()) {
				q.setParameter("sdate", filter.getMilestoneDateRange()[0]);
				q.setParameter("edate", filter.getMilestoneDateRange()[1]);
			}

			q.setFirstResult(pageRequest.getOffset());
			q.setMaxResults(pageRequest.getLimit());

			return q.getResultList();
		} finally {
			em.close();
		}
	}
}
