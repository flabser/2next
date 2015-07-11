package cashtracker.model;

import java.sql.ResultSet

import cashtracker.model.constants.TransactionState
import cashtracker.model.constants.TransactionType

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject
import com.flabser.users.User


@JsonRootName("transaction")
public class Transaction implements _IObject {

	private long id;

	private User user;

	private TransactionType transactionType;

	private TransactionState transactionState;

	private Date regDate = new Date();

	private Account accountFrom;

	private Account accountTo;

	private BigDecimal amount;

	private float exchangeRate;

	private Category category;

	private CostCenter costCenter;

	private List <Tag> tags;

	private boolean repeat;

	private int every;

	private int repeatStep;

	private Date startDate = new Date();

	private Date endDate = new Date();

	private String basis;

	private String note;

	private boolean includeInReports;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionState getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(TransactionState transactionState) {
		this.transactionState = transactionState;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Account getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(Account account) {
		this.accountFrom = account;
	}

	public Account getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(Account account) {
		this.accountTo = account;
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

	public CostCenter getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
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
		return "Transaction[$id, $user, $regDate, $startDate, $endDate, $category, $accountFrom, $amount, $costCenter]";
	}

	public void init(ResultSet rs) {
		setId(rs.getInt("id"));
		setUser(null);
		setTransactionType(TransactionType.typeOf(rs.getInt("transaction_type")))
		setTransactionState(TransactionState.stateOf(rs.getInt("transaction_state")))
		setRegDate(rs.getDate("reg_date"));
		setAccountFrom(null);
		setAccountTo(null);
		setAmount(rs.getBigDecimal("amount"));
		setExchangeRate(rs.getFloat("exchange_rate"));
		setCategory(null);
		setCostCenter(null);
		setTags(rs.getArray("tags"));
		setRepeat(rs.getBoolean("repeat"));
		setEvery(rs.getInt("every"));
		setRepeatStep(rs.getInt("repeat_step"));
		setStartDate(rs.getDate("start_date"));
		setEndDate(rs.getDate("end_date"));
		setBasis(rs.getString("basis"));
		setNote(rs.getString("note"));
		setIncludeInReports(rs.getBoolean("include_in_reports"))
	}
}
