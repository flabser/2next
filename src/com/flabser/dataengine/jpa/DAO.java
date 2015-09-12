package com.flabser.dataengine.jpa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;

import cashtracker.model.Transaction;

import com.flabser.env.Environment;
import com.flabser.script._Session;
import com.flabser.server.Server;
import com.flabser.users.User;

public abstract class DAO implements IDAO {
	protected EntityManager em;
	protected User user;

	public DAO(_Session session) {
		this.user = session.getAppUser();
		this.em = session.getDatabase().getEntityManager();
	}

	public IAppEntity add(IAppEntity entity) {
		File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
		em.getTransaction().begin();
		entity.setAuthor(user.id);
		entity.setRegDate(new Date());
		List<byte[]> files = new ArrayList<byte[]>();
		List<Attachment> attachments = entity.getAttachments();
		if (attachments != null) {
			for (Attachment a : attachments) {
				Transaction t = (Transaction) entity;
				String fieldName = a.getFieldName();
				String uploadedFileLocation = userTmpDir + File.separator + a.getTempID();
				File file = new File(uploadedFileLocation);
				byte[] bFile;
				try {
					bFile = FileUtils.readFileToByteArray(file);
					files.add(bFile);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}
			}

		}
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}

	public IAppEntity update(IAppEntity entity) {
		File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
		em.getTransaction().begin();
		List<byte[]> files = new ArrayList<byte[]>();
		Transaction t = (Transaction) entity;
		for (Attachment a : entity.getAttachments()) {
			String fieldName = a.getFieldName();
			String uploadedFileLocation = userTmpDir + File.separator + a.getTempID();
			File file = new File(uploadedFileLocation);
			byte[] bFile;
			try {
				bFile = FileUtils.readFileToByteArray(file);
				files.add(bFile);
			} catch (IOException e) {
				Server.logger.errorLogEntry(e);
			}

		}
		em.merge(entity);
		em.getTransaction().commit();
		return entity;
	}

	public void delete(IAppEntity entity) {
		em.getTransaction().begin();
		em.remove(entity);
		em.getTransaction().commit();
	}

}
