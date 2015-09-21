package com.flabser.dataengine.jpa;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;

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
		em.getTransaction().begin();
		entity.setAuthor(user.id);
		entity.setRegDate(new Date());
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}

	public IAppEntity update(IAppEntity entity) {
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
		return entity;
	}

	public void delete(IAppEntity entity) {
		em.getTransaction().begin();
		em.remove(entity);
		em.getTransaction().commit();
	}

	protected Set<Attachment> proccesAttachments(IAppEntity entity, Set<?> attachments){
		if (attachments != null) {
			File userTmpDir = new File(Environment.tmpDir + File.separator + user.getLogin());
			Set<Attachment> files = new HashSet<Attachment>();
			for (Object o : attachments) {
				Attachment a = (Attachment)o;
				//	String fieldName = a.getFieldName();
				String uploadedFileLocation = userTmpDir + File.separator + a.getTempID();
				File file = new File(uploadedFileLocation);
				Attachment attachEntity = new Attachment();
				try {
					byte[] bFile = FileUtils.readFileToByteArray(file);
					attachEntity.setFile(bFile);
					attachEntity.setRealFileName(file.getName());
					attachEntity.setParent(entity);
				} catch (IOException e) {
					Server.logger.errorLogEntry(e);
				}
				files.add(attachEntity);
			}
			return files;
		}else{
			return null;
		}
	}

}
