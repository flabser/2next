package com.flabser.restful.data;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;

@MappedSuperclass
public abstract class AppEntity implements IAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	protected long parentId;

	protected Long author;

	protected Date regDate;

	@Transient
	protected ArrayList<AttachedFile> attachments = new ArrayList<AttachedFile>();

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	class AttachedFile {
		public String fieldName;
		public String realFileName;
		public String tempID;
	}

	@Override
	public long getAuthor() {
		return author;
	}

	@Override
	public void setAuthor(Long author) {
		this.author = author;
	}

	public String getAuthorName() {
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		return sysDb.getUser(author).getLogin();
	}

	@Override
	public Date getRegDate() {
		return regDate;
	}

	@Override
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

}
