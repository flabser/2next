package com.flabser.dataengine.jpa;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("attachedFile")
public class AttachedFile {

	private String fieldName;
	private String realFileName;
	private String tempID;

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRealFileName() {
		return realFileName;
	}
	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}
	public String getTempID() {
		return tempID;
	}
	public void setTempID(String tempID) {
		this.tempID = tempID;
	}
}