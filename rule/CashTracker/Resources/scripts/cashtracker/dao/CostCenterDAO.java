package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.model.CostCenter;

import com.flabser.script._Session;
import com.flabser.users.User;


public class CostCenterDAO {

	private EntityManager em;
	private User user;

	public CostCenterDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
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
		return q.getResultList().size() > 0;
	}

	public CostCenter add(CostCenter m) {
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public CostCenter update(CostCenter m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete(CostCenter m) {
		em.getTransaction().begin();
		em.remove(m);
		em.getTransaction().commit();
	}
}
