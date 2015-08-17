package cashtracker.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("costCenter")
@Entity
@Table(name = "costcenters")
public class CostCenter extends AppEntity {

	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "costCenter")
	private List <Transaction> transactions;

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
