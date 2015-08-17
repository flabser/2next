package cashtracker.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import cashtracker.model.constants.BudgetState;
import cashtracker.model.constants.BudgetStateConverter;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("budget")
@Entity
@Table(name = "budgets")
public class Budget extends AppEntity {

	@Column(nullable = false)
	private String name;

	@Column(name = "reg_date")
	private Date regDate;

	private Long owner;

	@Convert(converter = BudgetStateConverter.class)
	private BudgetState status;

	//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
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

	@Override
	public String toString() {
		return "Budget[" + id + ", " + name + ", " + regDate + ", " + owner + ", " + status + "]";
	}
}
