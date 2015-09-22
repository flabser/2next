package cashtracker.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flabser.dataengine.jpa.SimpleAppEntity;


@Entity
@Table(name = "transaction_files")
public class TransactionFile extends SimpleAppEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	private String fieldName;
	private String realFileName;
	@Transient
	private String tempID;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] file;

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

	@JsonIgnore
	public byte[] getFile() {
		return file;
	}


	@JsonIgnore
	public void setFile(byte[] file) {
		this.file = file;
	}


	public void setTransaction(Transaction entity) {
		transaction = entity;

	}

	public Transaction getTransaction() {
		return transaction;

	}


}
