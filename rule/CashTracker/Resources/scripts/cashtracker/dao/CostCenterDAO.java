package cashtracker.dao;

import java.util.List;

import javax.persistence.Query;

import cashtracker.model.CostCenter;

import com.flabser.restful.data.DAO;
import com.flabser.script._Session;


public class CostCenterDAO extends DAO {

	public CostCenterDAO(_Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List <CostCenter> findAll() {
		String jpql = "SELECT cc FROM CostCenter AS cc ORDER BY cc.name";
		Query q = em.createQuery(jpql);
		return q.getResultList();
	}

	public CostCenter findById(long id) {
		String jpql = "SELECT cc FROM CostCenter AS cc WHERE cc.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (CostCenter) q.getSingleResult();
	}

	public boolean existsTransactionByCostCenter(CostCenter m) {
		String jpql = "SELECT t.id FROM Transaction AS t WHERE t.costCenter = :costCenter";
		Query q = em.createQuery(jpql);
		q.setParameter("costCenter", m);
		q.setMaxResults(1);
		return !q.getResultList().isEmpty();
	}
}
