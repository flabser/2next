package cashtracker.model;

import java.sql.ResultSet

import cashtracker.model.constants.BudgetState

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject
import com.flabser.users.User


@JsonRootName("budget")
public class Budget implements _IObject {

	private long id;

	private String name;

	private Date regDate;

	private User owner;

	private BudgetState status;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User user) {
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
		return """{
					"id": $id,
					"name": "$name",
					"regDate": "$regDate",
					"owner": $owner,
					"status": $status
				}""";
	}

	@Override
	public String getTableName() {
		return "budgets";
	}

	@Override
	public void init(ResultSet rs) {
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setRegDate(rs.getDate("reg_date"));
		setOwner((User) null);
		setStatus(BudgetState.stateOf(rs.getInt("type")));
	}
}
