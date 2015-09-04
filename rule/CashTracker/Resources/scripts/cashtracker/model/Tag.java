package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("tag")
@Entity
@Table(name = "tags")
public class Tag extends AppEntity {

	@Column(nullable = false, unique = true, length = 32)
	private String name;

	// @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
	// private List <Transaction> transactions;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag[" + id + ", " + name + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
