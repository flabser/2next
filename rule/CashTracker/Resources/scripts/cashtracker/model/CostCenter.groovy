package cashtracker.model;

import java.sql.ResultSet;

import com.flabser.script._IObject;

public class CostCenter implements _IObject {

	private long id;

	private int type;

	private String name;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CostCenter[" + name + "]";
	}

	public void init(ResultSet rs) {
		setId(rs.getInt("id"))
		setName(rs.getString("name"))
		setType(0)
	}
}
