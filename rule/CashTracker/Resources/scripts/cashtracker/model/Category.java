package cashtracker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionTypeConverter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.AppEntity;


@JsonRootName("category")
@Entity
@Table(name = "categories")
public class Category extends AppEntity {

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parentCategory;

	@OneToMany(mappedBy = "parentCategory")
	private List <Category> children;

	@JsonIgnore
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String name;

	@ElementCollection
	@Convert(converter = TransactionTypeConverter.class)
	@CollectionTable(name = "category_transactiontypes", joinColumns = @JoinColumn(name = "CATEGORY_ID"))
	@Column(name = "transaction_types")
	private List <TransactionType> transactionTypes;

	private boolean enabled;

	private String note;

	private int color;

	@Column(name = "sort_order")
	private int sortOrder;

	//
	public Category getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}

	@JsonGetter("parentCategory")
	public Long getParentCategoryId() {
		if (parentCategory == null || parentCategory.id == 0) {
			return null;
		}
		return parentCategory.id;
	}

	@JsonSetter("parentCategory")
	public void setParentCategoryId(Long id) {
		if (id == null || id == 0) {
			setParentCategory(null);
			return;
		}

		Category parent = new Category();
		parent.setId(id);
		setParentCategory(parent);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List <TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List <TransactionType> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	@JsonGetter("transactionTypes")
	public List <String> getTransactionTypesValue() {
		List <String> values = new ArrayList <String>();
		if (transactionTypes != null) {
			transactionTypes.forEach(type -> values.add(type.toValue()));
		}
		return values;
	}

	@JsonSetter("transactionTypes")
	public void setTransactionTypesByValues(List <String> values) {
		List <TransactionType> types = new ArrayList <TransactionType>();
		if (values != null) {
			values.forEach(value -> types.add(TransactionType.typeOf(value)));
		}
		setTransactionTypes(types);
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
		return "Category[" + id + ", " + name + ", " + enabled + ", " + parentCategory + ", " + transactionTypes + "]";
	}
}
