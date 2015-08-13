package com.flabser.restful.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Entity implements IEntity {
	@EntityField()
	protected long id;
	protected ArrayList<AttachedFile> attachments = new ArrayList<AttachedFile>();

	@Override
	public abstract void init(ResultSet rs) throws SQLException;

	@Override
	public String getTableName() {
		return getClass().getSimpleName() + "s";

	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public boolean isPermissionsStrict() {
		return false;
	}

	class AttachedFile {
		public String fieldName;
		public String realFileName;
		public String tempID;
	}

}
