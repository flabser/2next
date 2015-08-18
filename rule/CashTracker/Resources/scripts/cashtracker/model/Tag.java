package cashtracker.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("tag")
@Entity
@Table(name = "tags")
public class Tag extends AppEntity {

	@Column(nullable = false)
	private String name;

	@ManyToMany(mappedBy = "tags")
	private List <Transaction> transactions;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List <Transaction> getTransactions() {
		return transactions;
	}

	@Override
	public String toString() {
		return "Tag[" + id + ", " + name + "]";
	}
}
