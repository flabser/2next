package cashtracker.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;
import cashtracker.model.constants.converter.TransactionStateConverter;
import cashtracker.model.constants.converter.TransactionTypeConverter;
import cashtracker.serializers.JsonDateSerializer;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("transaction")
@Entity
@Table(name = "transactions")
@NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction AS t ORDER BY t.date ASC")
public class Transaction extends AppEntity /*SecureAppEntity*/{

	@Convert(converter = TransactionTypeConverter.class)
	@Column(name = "transaction_type", nullable = false, length = 3)
	private TransactionType transactionType = TransactionType.EXPENSE;

	@Convert(converter = TransactionStateConverter.class)
	@Column(name = "transaction_state", length = 3)
	private TransactionState transactionState;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	private Account account;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transfer_account")
	private Account transferAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cost_center")
	private CostCenter costCenter;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "transaction_tags", joinColumns = { @JoinColumn(name = "transaction_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") })
	private List <Tag> tags;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
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

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = true)
	private Date startDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = true)
	private Date endDate;

	@Column(length = 256)
	private String note;

	@Column(name = "include_in_reports")
	private boolean includeInReports;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "transaction", orphanRemoval = true, cascade = CascadeType.ALL)
	public Set <TransactionFile> attachments;

	//
	public Set <TransactionFile> getAttachments() {
		return attachments;
	}

	public TransactionFile getAttachment(String fieldName, String fileName) {
		for (TransactionFile file : attachments) {
			if (file.getFieldName().equalsIgnoreCase(fieldName) && file.getRealFileName().equals(fileName)) {
				return file;
			}
		}
		return null;
	}

	public boolean deleteAttachment(String fieldName, String fileName) {
		for (TransactionFile file : attachments) {
			if (file.getFieldName().equalsIgnoreCase(fieldName) && file.getRealFileName().equals(fileName)) {
				boolean res = attachments.remove(file);
				return res;
			}
		}
		return false;
	}

	public void setAttachments(Set <TransactionFile> attachments) {
		this.attachments = attachments;
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

	/*@JsonGetter("category")
	public Long getCategoryId() {
		if (category != null) {
			return category.getId();
		}
		return null;
	}*/

	// @JsonSetter("category")
	public void setCategoryById(Long id) {
		Category category = null;
		if (id != null) {
			category = new Category();
			category.setId(id);
		}
		setCategory(category);
	}

	public Account getAccount() {
		if (account == null || account.getId() == 0) {
			return null;
		}
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	/*@JsonGetter("account")
	public Long getAccountId() {
		if (account != null) {
			return account.getId();
		}
		return null;
	}*/

	// @JsonSetter("account")
	public void setAccountById(Long id) {
		Account account = null;
		if (id != null) {
			account = new Account();
			account.setId(id);
		}
		setAccount(account);
	}

	public Account getTransferAccount() {
		if (transferAccount == null || transferAccount.getId() == 0) {
			return null;
		}
		return transferAccount;
	}

	public void setTransferAccount(Account account) {
		this.transferAccount = account;
	}

	/*@JsonGetter("transferAccount")
	public Long getTransferAccountId() {
		if (transferAccount != null) {
			return transferAccount.getId();
		}
		return null;
	}*/

	// @JsonSetter("transferAccount")
	public void setTransferAccountById(Long id) {
		Account transferAccount = null;
		if (id != null) {
			transferAccount = new Account();
			transferAccount.setId(id);
		}
		setTransferAccount(transferAccount);
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

	/*@JsonGetter("costCenter")
	public Long getCostCenterId() {
		if (costCenter != null) {
			return costCenter.getId();
		}
		return null;
	}*/

	// @JsonSetter("costCenter")
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

	/*@JsonGetter("tags")
	public List <Long> getTagsId() {
		if (tags != null) {
			return tags.stream().map(Tag::getId).collect(Collectors.toList());
		}
		return null;
	}*/

	/*@JsonSetter("tags")
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
		return "Transaction[" + id + ", " + date + ", " + transactionType + ", " + category + ", " + account + ", "
				+ amount + ", " + costCenter + "," + tags + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
