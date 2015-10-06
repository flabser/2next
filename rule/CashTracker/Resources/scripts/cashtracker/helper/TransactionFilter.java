package cashtracker.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Tag;
import cashtracker.model.constants.TransactionType;


public class TransactionFilter {

	private List <TransactionType> transactionTypes;
	private List <Account> accounts;
	private List <Category> categories;
	private List <CostCenter> costCenters;
	private List <Tag> tags;
	private Date[] dateRange = new Date[2];

	public TransactionFilter() {
		transactionTypes = new ArrayList <TransactionType>();
		accounts = new ArrayList <Account>();
		categories = new ArrayList <Category>();
		costCenters = new ArrayList <CostCenter>();
		tags = new ArrayList <Tag>();
	}

	public List <TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List <TransactionType> types) {
		this.transactionTypes = types;
	}

	public void addTransactionType(TransactionType type) {
		transactionTypes.add(type);
	}

	public List <Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List <Account> accounts) {
		this.accounts = accounts;
	}

	public List <Category> getCategories() {
		return categories;
	}

	public void setCategories(List <Category> categories) {
		this.categories = categories;
	}

	public List <CostCenter> getCostCenters() {
		return costCenters;
	}

	public void setCostCenters(List <CostCenter> costCenters) {
		this.costCenters = costCenters;
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
	}

	public Date[] getDateRange() {
		return dateRange;
	}

	public void setDateRange(Date sd, Date ed) {
		if (sd.compareTo(ed) >= 0) {
			throw new IllegalArgumentException("st date < end date; " + sd + " : " + ed);
		}
		this.dateRange = new Date[] { sd, ed };
	}
}
