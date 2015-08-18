package cashtracker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.TransactionTypesConverter;

import com.fasterxml.jackson.annotation.JsonGetter;
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

	@Column(nullable = false)
	private String name;

	@Convert(converter = TransactionTypesConverter.class)
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

	public List <TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List <TransactionType> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	@JsonGetter("transactionTypes")
	public List <String> getTransactionTypesCode() {
		List <String> tTypes = new ArrayList <String>();
		if (transactionTypes != null) {
			transactionTypes.forEach(type -> tTypes.add(type.name()));
		}
		return tTypes;
	}

	@JsonSetter("transactionTypes")
	public void setTransactionTypesByIds(List <String> names) {
		List <TransactionType> tTypes = new ArrayList <TransactionType>();
		if (names != null) {
			names.forEach(name -> tTypes.add(TransactionType.valueOf(name)));
		}
		setTransactionTypes(tTypes);
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
