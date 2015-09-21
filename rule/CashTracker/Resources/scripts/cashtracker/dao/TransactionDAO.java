package cashtracker.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import cashtracker.helper.PageRequest;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;
import cashtracker.model.TransactionFile;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.jpa.Attachment;
import com.flabser.dataengine.jpa.DAO;
import com.flabser.script._Session;


public class TransactionDAO extends DAO {

	public TransactionDAO(_Session session) {
		super(session);
	}

	@SuppressWarnings("unchecked")
	public List <Transaction> find(PageRequest pr, TransactionType type) {
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

	@Override
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

	public Transaction add(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		entity.setAuthor(user.id);
		entity.setRegDate(new Date());
		//
		Set <Attachment> f = proccesAttachments(entity, entity.getAttachments());
		if (f != null) {
			Set <TransactionFile> files = new HashSet <TransactionFile>();
			for (Attachment ae : f) {
				ae.setParent(entity);
				//		files.add((TransactionFile) ae);
			}
			entity.setAttachments(files);
		}
		//
		em.persist(entity);
		transact.commit();
		return entity;
	}

	public Transaction update(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		//
		Set <Attachment> f = proccesAttachments(entity, entity.getAttachments());
		Set <TransactionFile> files = new HashSet <TransactionFile>();
		for (Attachment ae : f) {
			System.out.println(ae.getClass().getName());
			ae.setParent(entity);
			TransactionFile tf = new TransactionFile();
			tf.setFile(ae);
			files.add(tf);
		}
		entity.setAttachments(files);
		//
		em.merge(entity);
		transact.commit();
		return entity;
	}
}
