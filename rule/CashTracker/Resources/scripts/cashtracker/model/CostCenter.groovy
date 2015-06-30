package cashtracker.model;

import java.sql.ResultSet

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject


@JsonRootName("costCenter")
public class CostCenter implements _IObject {

	private long id;

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

	@Override
	public String toString() {
		return "CostCenter[" + name + "]";
	}

	public void init(ResultSet rs) {
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
	}
}
