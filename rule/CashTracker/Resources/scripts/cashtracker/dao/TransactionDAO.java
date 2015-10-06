package cashtracker.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;

import cashtracker.helper.PageRequest;
import cashtracker.helper.TransactionFilter;
import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Tag;
import cashtracker.model.Transaction;
import cashtracker.model.TransactionFile;
import cashtracker.model.constants.TransactionType;

import com.flabser.dataengine.jpa.DAO;
import com.flabser.env.Environment;
import com.flabser.script._Session;
import com.flabser.server.Server;


public class TransactionDAO extends DAO <Transaction, Long> {

	private final static String SELECT_ALL = "SELECT t FROM Transaction AS t";

	public TransactionDAO(_Session session) {
		super(Transaction.class, session);
	}

	public List <Transaction> find(TransactionFilter filter, PageRequest pr) {
		String accouns = "";
		String categories = "";
		String costCenters = "";
		String tags = "";
		String transactionTypes = "";
		String dateRange = "";
		boolean hasWhere = false;

		if (!filter.getAccounts().isEmpty()) {
			accouns = " t.account IN :accounts";
			hasWhere = true;
		}
		if (!filter.getCategories().isEmpty()) {
			categories = (hasWhere ? " AND " : "") + " t.category IN :categories";
			hasWhere = true;
		}
		if (!filter.getCostCenters().isEmpty()) {
			costCenters = (hasWhere ? " AND " : "") + " t.costCenter IN :costCenters";
			hasWhere = true;
		}
		if (!filter.getTags().isEmpty()) {
			tags = (hasWhere ? " AND " : "") + " t.tags IN :tags";
			hasWhere = true;
		}
		if (!filter.getTransactionTypes().isEmpty()) {
			transactionTypes = (hasWhere ? " AND " : "") + " t.transactionType IN :transactionTypes";
			hasWhere = true;
		}
		if (filter.getDateRange()[0] != null) {
			dateRange = (hasWhere ? " AND " : "") + " t.date > :sdate AND t.date < :edate";
			hasWhere = true;
		}

		//
		String jpql = SELECT_ALL + (hasWhere ? " WHERE " : "") + accouns + categories + costCenters + tags
				+ transactionTypes + dateRange + " ORDER BY t.date ASC";

		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		if (!accouns.isEmpty()) {
			q.setParameter("accounts", filter.getAccounts());
		}
		if (!categories.isEmpty()) {
			q.setParameter("categories", filter.getCategories());
		}
		if (!costCenters.isEmpty()) {
			q.setParameter("costCenters", filter.getCostCenters());
		}
		if (!tags.isEmpty()) {
			q.setParameter("tags", filter.getTags());
		}
		if (!transactionTypes.isEmpty()) {
			q.setParameter("transactionTypes", filter.getTransactionTypes());
		}
		if (!dateRange.isEmpty()) {
			q.setParameter("sdate", filter.getDateRange()[0]);
			q.setParameter("edate", filter.getDateRange()[1]);
		}

		q.setFirstResult(pr.getOffset());
		q.setMaxResults(pr.getLimit());

		return q.getResultList();
	}

	public List <Transaction> findAllByAccount(Account m) {
		String jpql = SELECT_ALL + " WHERE t.account = :account";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("account", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByTransferAccount(Account m) {
		String jpql = SELECT_ALL + " WHERE t.transferAccount = :account";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("account", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByCostCenter(CostCenter m) {
		String jpql = SELECT_ALL + " WHERE t.costCenter = :costCenter";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("costCenter", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByCategory(Category m) {
		String jpql = SELECT_ALL + " WHERE t.category = :category";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("category", m);
		return q.getResultList();
	}

	public List <Transaction> findAllByTags(List <Tag> tags) {
		String jpql = SELECT_ALL + " WHERE t.tags IN :tags";
		TypedQuery <Transaction> q = em.createQuery(jpql, Transaction.class);
		q.setParameter("tags", tags);
		return q.getResultList();
	}

	public int getCountByType(TransactionType type) {
		Query q = em.createQuery("SELECT count(t) FROM Transaction AS t WHERE t.transactionType = :type",
				Transaction.class);
		q.setParameter("type", type);
		return ((Long) q.getSingleResult()).intValue();
	}

	@Override
	public Transaction add(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		entity.setAuthor(user.id);
		//
		Set <TransactionFile> files = proccesAttachments(entity, entity.getAttachments());
		if (files != null) {
			entity.setAttachments(files);
		}
		//
		em.persist(entity);
		transact.commit();
		return entity;
	}

	@Override
	public Transaction update(Transaction entity) {
		EntityTransaction transact = em.getTransaction();
		transact.begin();
		//
		Set <TransactionFile> files = proccesAttachments(entity, entity.getAttachments());
		if (files != null) {
			entity.setAttachments(files);
		}
		//
		em.merge(entity);
		transact.commit();
		return entity;
	}

	protected Set <TransactionFile> proccesAttachments(Transaction entity, Set <TransactionFile> attachments) {
		if (attachments != null) {
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			Set <TransactionFile> files = new HashSet <TransactionFile>();
			for (TransactionFile newFile : attachments) {
				String tmpID = newFile.getTempID();
				if (tmpID != null) {
					String uploadedFileLocation = userTmpDir + File.separator + newFile.getTempID();
					File file = new File(uploadedFileLocation);
					try {
						byte[] bFile = FileUtils.readFileToByteArray(file);
						newFile.setFile(bFile);
						newFile.setTransaction(entity);
					} catch (IOException e) {
						Server.logger.errorLogEntry(e);
					}
					files.add(newFile);
				}
			}
			return files;
		} else {
			return null;
		}
	}
}
