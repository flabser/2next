package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import cashtracker.helper.PageRequest;
import cashtracker.model.CostCenter;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class CostCenterDAO extends DAO <CostCenter, Long> {

	public CostCenterDAO(_Session session) {
		super(CostCenter.class, session);
	}

	public List <CostCenter> find(PageRequest pr) {
		TypedQuery <CostCenter> q = em.createNamedQuery("CostCenter.findAll", CostCenter.class);
		q.setFirstResult(pr.getOffset());
		q.setMaxResults(pr.getLimit());
		return q.getResultList();
	}

	public boolean existsTransactionByCostCenter(CostCenter m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.costCenter = :costCenter";
		Query q = em.createQuery(jpql);
		q.setParameter("costCenter", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
