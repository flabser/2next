package cashtracker.model;

import java.sql.ResultSet;

import com.flabser.script._IObject;

public class Category implements _IObject {

	public enum CategoryType {
		INCOME, EXPENSE;
	}

	private long id;

	private String name;

	private CategoryType type;

	private String comment;

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

	public CategoryType getType() {
		return type;
	}

	public void setType(CategoryType type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Category[" + name + "]";
	}

	public void init(ResultSet rs) {
		setId(rs.getInt("id"))
		setName(rs.getString("name"))
		setType(CategoryType.EXPENSE)
		setComment(rs.getString("comment"))
	}
}
