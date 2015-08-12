package com.flabser.restful.data;

import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class AppEntity implements IAppEntity {
	private long id;
	private ArrayList<AttachedFile> attachments = new ArrayList<AttachedFile>();

	public ArrayList<AttachedFile> getAttachments() {
		return attachments;
	}

	class AttachedFile {
		public String fieldName;
		public String realFileName;
		public String tempID;
	}

	@Override
	public abstract void init(ResultSet rs);

	@Override
	public String getName() {
		return null;

	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean isPermissionsStrict() {
		return false;
	}

}
