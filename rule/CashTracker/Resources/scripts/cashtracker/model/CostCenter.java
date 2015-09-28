package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("costCenter")
@Entity
@Table(name = "costcenters")
@NamedQuery(name = "CostCenter.findAll", query = "SELECT cc FROM CostCenter AS cc ORDER BY cc.name")
public class CostCenter extends AppEntity {

	@Column(nullable = false, unique = true, length = 64)
	private String name;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CostCenter[" + id + ", " + name + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
