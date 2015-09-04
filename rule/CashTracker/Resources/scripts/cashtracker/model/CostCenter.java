package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("costCenter")
@Entity
@Table(name = "costcenters")
public class CostCenter extends AppEntity {

	@Column(nullable = false, unique = true, length = 64)
	private String name;

	// @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY)
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
		return "CostCenter[" + id + ", " + name + "]";
	}
}
