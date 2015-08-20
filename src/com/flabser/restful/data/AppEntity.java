package com.flabser.restful.data;

import java.util.ArrayList;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AppEntity implements IAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	protected long parentId;

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

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	class AttachedFile {
		public String fieldName;
		public String realFileName;
		public String tempID;
	}

}
