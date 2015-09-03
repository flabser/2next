package cashtracker.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import cashtracker.model.constants.BudgetState;
import cashtracker.model.constants.converter.BudgetStateConverter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.AppEntity;


@JsonRootName("budget")
@Entity
@Table(name = "budgets")
public class Budget extends AppEntity {

	@Column(nullable = false)
	private String name;

	private Long owner;

	@Convert(converter = BudgetStateConverter.class)
	private BudgetState status;


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

	public BudgetState getStatus() {
		return status;
	}

	public void setStatus(BudgetState status) {
		this.status = status;
	}

	@JsonGetter("status")
	public String getStatusValue() {
		if (status == null) {
			return null;
		}
		return status.toValue();
	}

	@JsonSetter("status")
	public void setStatusByValue(String value) {
		setStatus(BudgetState.stateOf(value));
	}

	@Override
	public String toString() {
		return "Budget[" + id + ", " + name + ", " + regDate + ", " + owner + ", " + status + "]";
	}
}
