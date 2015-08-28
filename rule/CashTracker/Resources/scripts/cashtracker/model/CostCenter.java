package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("costCenter")
@Entity
@Table(name = "costcenters")
public class CostCenter extends AppEntity {

	@JsonIgnore
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String name;

	// @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY)
	// private List <Transaction> transactions;

	//
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
