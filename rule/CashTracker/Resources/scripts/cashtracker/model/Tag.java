package cashtracker.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.Entity;
import com.flabser.restful.data.EntityField;


@JsonRootName("tag")
public class Tag extends Entity {

	@EntityField("name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag[" + name + "]";
	}

	@Override
	public void init(ResultSet rs) throws SQLException {
		setId(rs.getLong("id"));
		setName(rs.getString("name"));
	}

	@Override
	@JsonIgnore
	public String getTableName() {
		return "tags";
	}
}
