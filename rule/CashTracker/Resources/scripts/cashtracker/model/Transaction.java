package cashtracker.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.AppEntity;


@JsonRootName("transaction")
@Entity
@Table(name = "transactions")
public class Transaction extends AppEntity /*SecureAppEntity*/ {

	@Column(nullable = false)
	private Long user;

	@Column(name = "transaction_type")
	@Enumerated(EnumType.ORDINAL)
	private TransactionType transactionType = TransactionType.EXPENSE;

	@Column(name = "transaction_state")
	@Enumerated(EnumType.ORDINAL)
	private TransactionState transactionState = TransactionState.UNKNOWN;

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

	// @ManyToMany
	// @JoinColumn(name = "tags")
	// private List <Tag> tags;

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

	@Column(name = "start_date")
	private Date startDate = new Date();

	@Column(name = "end_date")
	private Date endDate = new Date();

	private String basis;

	private String note;

	@Column(name = "include_in_reports")
	private boolean includeInReports;

	//
	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@JsonGetter("transactionType")
	public Integer getTransactionTypeCode() {
		return transactionType.getCode();
	}

	@JsonSetter("transactionType")
	public void setTransactionTypeById(int id) {
		setTransactionType(TransactionType.typeOf(id));
	}

	public TransactionState getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(TransactionState transactionState) {
		this.transactionState = transactionState;
	}

	@JsonGetter("transactionState")
	public Integer getTransactionStateCode() {
		return transactionState.getCode();
	}

	@JsonSetter("transactionState")
	public void setTransactionStateById(int id) {
		setTransactionState(TransactionState.stateOf(id));
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

	/*public List <Tag> getTags() {
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
	}*/

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

	public String getBasis() {
		return basis;
	}

	public void setBasis(String basis) {
		this.basis = basis;
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
		return "Transaction[" + id + ", " + user + ", " + date + ", " + startDate + ", " + endDate + ", " + category
				+ ", " + accountFrom + ", " + amount + ", " + costCenter + "]";
	}
}
