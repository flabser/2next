package cashtracker.helper;

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

	}

	public List <TransactionType> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTypes(List <TransactionType> types) {
		this.transactionTypes = types;
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

	public void setDateRange(Date[] dateRange) {
		if (dateRange[0].compareTo(dateRange[1]) < 0) {
			new IllegalArgumentException("st date < end date; " + dateRange[0] + " : " + dateRange[1]);
		}
		this.dateRange = dateRange;
	}
}
