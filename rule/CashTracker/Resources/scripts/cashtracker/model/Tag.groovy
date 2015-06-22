package cashtracker.model;

import java.sql.ResultSet
import java.sql.SQLException

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject


@JsonRootName("tag")
public class Tag implements _IObject {

	private long id;

	private String name;

	private int color;

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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Tag[" + name + "]";
	}

	public void init(ResultSet rs) {
		try {
			setId(rs.getInt("id"));
			setName(rs.getString("name"));
			setColor(rs.getInt("color"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
