package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("budget")
@Entity
@Table(name = "budgets")
@NamedQuery(name = "Budget.findAll", query = "SELECT b FROM Budget AS b")
public class Budget extends AppEntity {

	public enum BudgetStatus {
		ACTIVE, DELETED;
	}

	@Column(nullable = false, unique = true, length = 64)
	private String name;

	private Long owner;

	@Enumerated(EnumType.STRING)
	@Column(length = 16)
	private BudgetStatus status;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long user) {
		this.owner = user;
	}

	public BudgetStatus getStatus() {
		return status;
	}

	public void setStatus(BudgetStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Budget[" + id + ", " + name + ", " + owner + ", " + status + ", " + getAuthor() + ", " + getRegDate()
				+ "]";
	}
}
