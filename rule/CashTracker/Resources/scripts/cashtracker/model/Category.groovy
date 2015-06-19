package cashtracker.model;

import java.sql.ResultSet
import java.sql.SQLException

import com.flabser.script._IObject
import com.flabser.solutions.cashtracker.constants.TransactionType


public class Category implements _IObject {

	private long id;
	private TransactionType transactionType;
	private Category parentCategory;
	private String name;
	private String note;
	private int color;
	private int sortOrder;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "Category[" + name + "]";
	}

	public void init(ResultSet rs) {
		try {
			setId(rs.getInt("id"));
			setTransactionType(TransactionType.INCOME);
			// setParentCategory(rs.getInt("parent_id"));
			setName(rs.getString("name"));
			setNote(rs.getString("note"));
			setColor(rs.getInt("color"));
			setSortOrder(rs.getInt("sort_order"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
