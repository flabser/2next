package cashtracker.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import cashtracker.model.constants.TransactionState;
import cashtracker.model.constants.TransactionType;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.restful.data.ApplicationEntity;
import com.flabser.restful.data.ApplicationEntityField;

@JsonRootName("transaction")
public class Transaction extends ApplicationEntity {

	private long user;

	private TransactionType transactionType = TransactionType.EXPENSE;

	private TransactionState transactionState = TransactionState.UNKNOWN;

	private Account accountFrom;

	private Account accountTo;

	private Category category;

	private CostCenter costCenter;

	private List<Tag> tags;

	private Date date = new Date();

	private BigDecimal amount;

	private float exchangeRate;

	private boolean repeat;

	private int every;

	private int repeatStep;

	private Date startDate = new Date();

	private Date endDate = new Date();

	private String basis;

	private String note;

	private boolean includeInReports;

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	@ApplicationEntityField
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
	public void setCategoryId(Long id) {
		Category category = null;
		if (id != null) {
			category = new Category();
			category.setId(id);
		}
		setCategory(category);
	}

	public Account getAccountFrom() {
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@JsonGetter("tags")
	public Long getTagsId() {
		return null;
	}

	@JsonSetter("tags")
	public void setTagsById(Long id) {
		List<Tag> tags = null;
		if (id != null) {

		}
		setTags(tags);
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
		return "Transaction[$id, $user, $date, $startDate, $endDate, $category, $accountFrom, $amount, $costCenter]";
	}

	@Override
	public void init(ResultSet rs) throws SQLException {
		setId(rs.getInt("t.id"));
		// setUser(null);
		setTransactionType(TransactionType.typeOf(rs.getInt("t.transaction_type")));
		setTransactionState(TransactionState.stateOf(rs.getInt("t.transaction_state")));
		setDate(rs.getDate("t.date"));
		setAccountFrom(null);
		setAccountTo(null);
		setAmount(rs.getBigDecimal("t.amount"));
		setExchangeRate(rs.getFloat("t.exchange_rate"));
		setCategory(null);
		setCostCenter(null);
		// setTags(rs.getArray("t.tags"));
		setRepeat(rs.getBoolean("t.repeat"));
		setEvery(rs.getInt("t.every"));
		setRepeatStep(rs.getInt("t.repeat_step"));
		setStartDate(rs.getDate("t.start_date"));
		setEndDate(rs.getDate("t.end_date"));
		setNote(rs.getString("t.note"));
		setIncludeInReports(rs.getBoolean("t.include_in_reports"));
		setBasis(rs.getString("t.basis"));
	}

	@Override
	public String getTableName() {
		return "transactions";
	}

	@Override
	public boolean isPermissionsStrict() {
		return true;
	}
}
