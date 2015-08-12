package cashtracker.model;

import java.sql.ResultSet

import cashtracker.model.constants.TransactionType

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonSetter
import com.flabser.restful.data.AppEntity


@JsonRootName("category")
public class Category  extends AppEntity {

	private long id;

	private List<TransactionType> transactionTypes;

	private Category parentCategory;

	private String name;

	private boolean enabled;

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

	public List<TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List<TransactionType> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	@JsonGetter("transactionTypes")
	public List<Integer> getTransactionTypesCode() {
		List<Integer> transactionTypesCode = new ArrayList<Integer>();
		if (transactionTypes != null) {
			transactionTypes.each {
				transactionTypesCode.add(it.code);
			}
		}
		return transactionTypesCode;
	}

	@JsonSetter("transactionTypes")
	public void setTransactionTypesByIds(List<Integer> ids) {
		List<TransactionType> transactionTypes = new ArrayList<TransactionType>();
		if (ids != null) {
			ids.each {
				transactionTypes.add(TransactionType.typeOf(it));
			}
		}
		setTransactionTypes(transactionTypes);
	}

	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@JsonGetter("parentCategory")
	public Long getParentCategoryId() {
		if (parentCategory != null) {
			return parentCategory.id;
		}
		return null;
	}

	@JsonSetter("parentCategory")
	public void setParentCategoryById(Long id) {
		Category parentCategory = null;
		if (id != null) {
			parentCategory = new Category();
			parentCategory.setId(id);
		}
		setParentCategory(parentCategory)
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
		return "Category[$id, $name, $transactionTypes, $enabled, $parentCategory]";
	}

	@Override
	public void init(ResultSet rs) {
		setId(rs.getInt("id"));
		// setTransactionTypes(rs.getArray("transaction_type"));
		setParentCategory(null);
		setName(rs.getString("name"));
		setEnabled(rs.getBoolean("enabled"));
		setNote(rs.getString("note"));
		setColor(rs.getInt("color"));
		setSortOrder(rs.getInt("sort_order"));
	}
}
