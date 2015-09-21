package com.flabser.dataengine.jpa;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
	"fieldName",
	"realFileName",
	"tempID"
})

@Embeddable
public class Attachment implements ISimpleAppEntity {

	@Transient
	private String fieldName;
	private String realFileName;
	@Transient
	private String tempID;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	private IAppEntity parentEntity;


	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] file;

	@Override
	public void setId(long id) {
		this.id = id;
	}


	@Override
	public long getId() {
		return id;
	}


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

	@Transient
	public String getTempID() {
		return tempID;
	}


	@Transient
	public void setTempID(String tempID) {
		this.tempID = tempID;
	}


	@JsonIgnore
	public byte[] getFile() {
		return file;
	}



	public void setFile(byte[] file) {
		this.file = file;
	}


	public IAppEntity getParent() {
		return parentEntity;
	}


	public void setParent(IAppEntity parentEntity) {
		this.parentEntity = parentEntity;
	}
}