package cashtracker.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import cashtracker.model.constants.BudgetState;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;
import com.flabser.restful.data.EntityField;

@JsonRootName("budget")
public class Budget extends AppEntity {

	@EntityField()
	private String name;

	@EntityField("reg_date")
	private Date regDate;

	private Long owner;

	@EntityField("type")
	private BudgetState status;

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

	@Override
	public void init(ResultSet rs) throws SQLException {
		setId(rs.getLong("id"));
		setName(rs.getString("name"));
		setRegDate(rs.getDate("reg_date"));
		setOwner(null);
		setStatus(BudgetState.stateOf(rs.getInt("type")));
	}

}
