package cashtracker.model;

import java.sql.ResultSet

import com.fasterxml.jackson.annotation.JsonRootName
import com.flabser.script._IObject
import com.flabser.users.User


@JsonRootName("account")
public class Account implements _IObject {

	private long id;

	// private int type;

	private String name;

	private String currencyCode;

	private BigDecimal openingBalance;

	private BigDecimal amountControl;

	private boolean includeInTotals;

	private String note;

	private List <User> writers;

	private List <User> readers;

	private int sortOrder;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getAmountControl() {
		return amountControl;
	}

	public void setAmountControl(BigDecimal amountControl) {
		this.amountControl = amountControl;
	}

	public String getWriters() {
		return null; // writers;
	}

	public void setWriters(List <User> writers) {
		this.writers = writers;
	}

	public List <User> getReaders() {
		return null; //readers;
	}

	public void setReaders(List <User> readers) {
		this.readers = readers;
	}

	public boolean isIncludeInTotals() {
		return includeInTotals;
	}

	public void setIncludeInTotals(boolean includeInTotals) {
		this.includeInTotals = includeInTotals;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "Account[" + name + ", " + currencyCode + ", " + openingBalance + "]";
	}

	public void init(ResultSet rs) {
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setCurrencyCode(rs.getString("currency_code"));
		setOpeningBalance(rs.getBigDecimal("opening_balance"));
		setAmountControl(rs.getBigDecimal("amount_control"));
		setWriters(null);
		setReaders(null);
		// setObservers(rs.getString("observers"));
		setIncludeInTotals(rs.getBoolean("include_in_totals"));
		setNote(rs.getString("note"));
		setSortOrder(rs.getInt("sort_order"));
	}

    @Override
    public String getTableName() {
        return "account";
    }
}
