package cashtracker.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cashtracker.model.constants.TransactionType;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.Entity;


@JsonRootName("category")
public class Category extends Entity {

	private List <TransactionType> transactionTypes;

	private Category parentCategory;

	private String name;

	private boolean enabled;

	private String note;

	private int color;

	private int sortOrder;

	public List <TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List <TransactionType> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	@JsonGetter("transactionTypes")
	public List <Integer> getTransactionTypesCode() {
		List <Integer> tTypes = new ArrayList <Integer>();
		if (transactionTypes != null) {
			transactionTypes.forEach(type -> tTypes.add(type.getCode()));
		}
		return tTypes;
	}

	@JsonSetter("transactionTypes")
	public void setTransactionTypesByIds(List <Integer> ids) {
		List <TransactionType> tTypes = new ArrayList <TransactionType>();
		if (ids != null) {
			ids.forEach(id -> tTypes.add(TransactionType.typeOf(id)));
		}
		setTransactionTypes(tTypes);
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@JsonGetter("parentCategory")
	public Long getParentCategoryId() {
		if (parentCategory != null && parentCategory.id != 0) {
			return parentCategory.id;
		}
		return null;
	}

	@JsonSetter("parentCategory")
	public void setParentCategoryId(Long id) {
		Category parentCategory = null;
		if (id != null) {
			parentCategory = new Category();
			parentCategory.setId(id);
		}
		setParentCategory(parentCategory);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		return "Category[" + id + ", " + name + ", " + transactionTypes + ", " + enabled + ", " + parentCategory + "]";
	}

	@Override
	public void init(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		// setTransactionTypes(rs.getArray("transaction_type"));
		setParentCategoryId(rs.getLong("parent_id"));
		setName(rs.getString("name"));
		setEnabled(rs.getBoolean("enabled"));
		setNote(rs.getString("note"));
		setColor(rs.getInt("color"));
		setSortOrder(rs.getInt("sort_order"));
	}

	@Override
	@JsonIgnore
	public String getTableName() {
		return "categories";
	}
}
