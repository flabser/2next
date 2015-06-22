package cashtracker.model;

import java.sql.ResultSet
import java.sql.SQLException

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject
import com.flabser.solutions.cashtracker.constants.BudgetState
import com.flabser.users.User


@JsonRootName("budget")
public class Budget implements _IObject {

	private long id;

	private String name;

	private Date regDate;

	private String owner;

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

	public String getOwner() {
		return owner;
	}

	public void setOwner(User user) {
		this.owner = user.getLogin();
	}

	public BudgetState getStatus() {
		return status;
	}

	public void setStatus(BudgetState status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Budget[" + name + "]";
	}

	public void init(ResultSet rs) {
		try {
			setId(rs.getInt("id"));
			setName(rs.getString("name"));
			setRegDate(rs.getDate("regdate"));
			setOwner((User) null);
			setStatus(BudgetState.stateOf(rs.getInt("type")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
