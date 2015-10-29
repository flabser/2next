package cashtracker.dao;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.io.FileUtils;

import cashtracker.helper.PageRequest;
import cashtracker.helper.TransactionFilter;
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

		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
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
		} finally {
			em.close();
		}
	}

	public int getCountByType(TransactionType type) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("SELECT count(t) FROM Transaction AS t WHERE t.transactionType = :type",
					Transaction.class);
			q.setParameter("type", type);
			return ((Long) q.getSingleResult()).intValue();
		} finally {
			em.close();
		}
	}

	@Override
	public Transaction add(Transaction entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction transact = em.getTransaction();
			try {
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
			} finally {
				if (transact.isActive()) {
					transact.rollback();
				}
			}
		} finally {
			em.close();
		}
	}

	@Override
	public Transaction update(Transaction entity) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		try {
			EntityTransaction transact = em.getTransaction();
			try {
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
			} finally {
				if (transact.isActive()) {
					transact.rollback();
				}
			}
		} finally {
			em.close();
		}
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
