package com.flabser.dataengine.jpa;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;


@MappedSuperclass
public abstract class AppEntity implements IAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@Column(name = "author", nullable = false, updatable = false)
	protected Long author;

	@Column(name = "reg_date", nullable = false, updatable = false)
	protected Date regDate;

	//@Transient
	@ElementCollection
	@CollectionTable(name = "attachments",joinColumns =  @JoinColumn(name = "attachments_fk_parent"))
	@Column(name = "attachments")
	protected ArrayList <AttachedFile> attachments = new ArrayList <AttachedFile>();

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Long getAuthor() {
		return author;
	}

	@Override
	public void setAuthor(Long author) {
		this.author = author;
	}

	public String getAuthorName() {
		if (author == null) {
			return null;
		}

		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		return sysDb.getUser(author).getUserName();
	}

	@Override
	public Date getRegDate() {
		return regDate;
	}

	@Override
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public ArrayList<AttachedFile> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<AttachedFile> attachments) {
		this.attachments = attachments;
	}
}
