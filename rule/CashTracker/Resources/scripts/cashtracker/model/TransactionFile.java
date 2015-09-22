package cashtracker.model;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flabser.dataengine.jpa.Attachment;


@Entity
@Table(name = "transaction_files")
public class TransactionFile {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	@Embedded
	private Attachment file;

	public Attachment getFile() {
		return file;
	}

	public void setFile(Attachment file) {
		this.file = file;
	}
}
