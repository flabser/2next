package cashtracker.model;

import java.sql.ResultSet

import cashtracker.model.constants.TransactionType

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject


@JsonRootName("category")
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
		setId(rs.getInt("id"));
		setTransactionType(TransactionType.typeOf(rs.getInt("transaction_type")));
		setParentCategory(null);
		setName(rs.getString("name"));
		setNote(rs.getString("note"));
		setColor(rs.getInt("color"));
		setSortOrder(rs.getInt("sort_order"));
	}

    @Override
    public String getTableName() {
        return "category";
    }
}
