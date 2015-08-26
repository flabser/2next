package cashtracker.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cashtracker.helper.PageRequest;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionType;

import com.flabser.script._Session;
import com.flabser.users.User;


public class TransactionDAO {

	private EntityManager em;
	private User user;

	public TransactionDAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	@SuppressWarnings("unchecked")
	public List <Transaction> findAll(PageRequest pr, TransactionType type) {
		String jpql;
		if (type == null) {
			jpql = "SELECT t FROM Transaction AS t ORDER BY t.date";
		} else {
			jpql = "SELECT t FROM Transaction AS t WHERE t.transactionType = :type ORDER BY t.date";
		}

		Query q = em.createQuery(jpql);
		if (type != null) {
			q.setParameter("type", type);
		}
		q.setFirstResult(pr.getOffset());
		q.setMaxResults(pr.getLimit());

		List <Transaction> result = q.getResultList();
		return result;
	}

	public Transaction findById(long id) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.id = :id";
		Query q = em.createQuery(jpql);
		q.setParameter("id", id);
		return (Transaction) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List <Transaction> findAllByAccountFrom(Account m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.accountFrom = :account";
		Query q = em.createQuery(jpql);
		q.setParameter("account", m);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List <Transaction> findAllByCostCenter(CostCenter m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.costCenter = :costCenter";
		Query q = em.createQuery(jpql);
		q.setParameter("costCenter", m);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List <Transaction> findAllByCategory(Category m) {
		String jpql = "SELECT t FROM Transaction AS t WHERE t.category = :category";
		Query q = em.createQuery(jpql);
		q.setParameter("category", m);
		return q.getResultList();
	}

	public Transaction add(Transaction m) {
		m.setUserId((long) user.id);
		em.getTransaction().begin();
		em.persist(m);
		em.getTransaction().commit();
		return m;
	}

	public Transaction update(Transaction m) {
		em.getTransaction().begin();
		em.merge(m);
		em.getTransaction().commit();
		return m;
	}

	public void delete(Transaction m) {
		em.getTransaction().begin();
		em.remove(m);
		em.getTransaction().commit();
	}
}
