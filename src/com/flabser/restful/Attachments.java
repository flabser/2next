package com.flabser.restful;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.flabser.script._IObject;

public abstract class Attachments implements _IObject {
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

}
