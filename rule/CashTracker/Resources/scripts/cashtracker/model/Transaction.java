package cashtracker.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionStateConverter;
import cashtracker.model.constants.converter.TransactionTypeConverter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.AppEntity;


@JsonRootName("transaction")
@Entity
@Table(name = "transactions")
public class Transaction extends AppEntity /*SecureAppEntity*/{

	@Column(nullable = false)
	private Long userId;

	@Convert(converter = TransactionTypeConverter.class)
	@Column(name = "transaction_type")
	private TransactionType transactionType = TransactionType.EXPENSE;

	@Convert(converter = TransactionStateConverter.class)
	@Column(name = "transaction_state")
	private TransactionState transactionState;

	@ManyToOne
	@JoinColumn(name = "account_from", nullable = false)
	private Account accountFrom;

	@ManyToOne
	@JoinColumn(name = "account_to")
	private Account accountTo;

	@ManyToOne
	@JoinColumn(name = "category")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "cost_center")
	private CostCenter costCenter;

	@ManyToMany
	@JoinTable(name = "transaction_tags", joinColumns = { @JoinColumn(name = "TRANSACTION_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "TAG_ID", referencedColumnName = "ID") })
	private List <Tag> tags;

	@Column(nullable = false)
	private Date date = new Date();

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(name = "exchange_rate")
	private float exchangeRate;

	private boolean repeat;

	private int every;

	@Column(name = "repeat_step")
	private int repeatStep;

	@Column(name = "start_date", nullable = true)
	private Date startDate;

	@Column(name = "end_date", nullable = true)
	private Date endDate;

	private String note;

	@Column(name = "include_in_reports")
	private boolean includeInReports;

	//
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@JsonGetter("transactionType")
	public String getTransactionTypeValue() {
		if (transactionType == null) {
			return null;
		}
		return transactionType.toValue();
	}

	@JsonSetter("transactionType")
	public void setTransactionTypeByValue(String value) {
		setTransactionType(TransactionType.typeOf(value));
	}

	public TransactionState getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(TransactionState transactionState) {
		this.transactionState = transactionState;
	}

	@JsonGetter("transactionState")
	public String getTransactionStateValue() {
		if (transactionState == null) {
			return null;
		}
		return transactionState.toValue();
	}

	@JsonSetter("transactionState")
	public void setTransactionStateByValue(String value) {
		setTransactionState(TransactionState.stateOf(value));
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@JsonGetter("category")
	public Long getCategoryId() {
		if (category != null) {
			return category.getId();
		}
		return null;
	}

	@JsonSetter("category")
	public void setCategoryById(Long id) {
		Category category = null;
		if (id != null) {
			category = new Category();
			category.setId(id);
		}
		setCategory(category);
	}

	public Account getAccountFrom() {
		if (accountFrom == null || accountFrom.getId() == 0) {
			return null;
		}
		return accountFrom;
	}

	public void setAccountFrom(Account account) {
		this.accountFrom = account;
	}

	@JsonGetter("accountFrom")
	public Long getAccountFromId() {
		if (accountFrom != null) {
			return accountFrom.getId();
		}
		return null;
	}

	@JsonSetter("accountFrom")
	public void setAccountFromById(Long id) {
		Account accountFrom = null;
		if (id != null) {
			accountFrom = new Account();
			accountFrom.setId(id);
		}
		setAccountFrom(accountFrom);
	}

	public Account getAccountTo() {
		if (accountTo == null || accountTo.getId() == 0) {
			return null;
		}
		return accountTo;
	}

	public void setAccountTo(Account account) {
		this.accountTo = account;
	}

	@JsonGetter("accountTo")
	public Long getAccountToId() {
		if (accountTo != null) {
			return accountTo.getId();
		}
		return null;
	}

	@JsonSetter("accountTo")
	public void setAccountToById(Long id) {
		Account accountTo = null;
		if (id != null) {
			accountTo = new Account();
			accountTo.setId(id);
		}
		setAccountTo(accountTo);
	}

	public CostCenter getCostCenter() {
		if (costCenter == null || costCenter.getId() == 0) {
			return null;
		}
		return costCenter;
	}

	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
	}

	@JsonGetter("costCenter")
	public Long getCostCenterId() {
		if (costCenter != null) {
			return costCenter.getId();
		}
		return null;
	}

	@JsonSetter("costCenter")
	public void setCostCenterById(Long id) {
		CostCenter costCenter = null;
		if (id != null) {
			costCenter = new CostCenter();
			costCenter.setId(id);
		}
		setCostCenter(costCenter);
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
	}

	@JsonGetter("tags")
	public List <Long> getTagsId() {
		if (tags != null) {
			return tags.stream().map(Tag::getId).collect(Collectors.toList());
		}
		return null;
	}

	@JsonSetter("tags")
	public void setTagsId(List <Long> ids) {
		if (ids != null) {
			List <Tag> _tags = new ArrayList <Tag>();
			ids.forEach(id -> {
				Tag tag = new Tag();
				tag.setId(id);
				_tags.add(tag);
			});
			setTags(_tags);
		} else {
			setTags(null);
		}
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date date) {
		this.startDate = date;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public float getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public int getEvery() {
		return every;
	}

	public void setEvery(int every) {
		this.every = every;
	}

	public int getRepeatStep() {
		return repeatStep;
	}

	public void setRepeatStep(int repeatStep) {
		this.repeatStep = repeatStep;
	}

	public boolean isIncludeInReports() {
		return includeInReports;
	}

	public void setIncludeInReports(boolean includeInReports) {
		this.includeInReports = includeInReports;
	}

	@Override
	public String toString() {
		return "Transaction[" + id + ", " + userId + ", " + date + ", " + category + ", " + accountFrom + ", " + amount
				+ ", " + costCenter + "," + tags + "]";
	}
}
