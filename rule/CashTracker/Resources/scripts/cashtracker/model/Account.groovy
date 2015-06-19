package cashtracker.model;

import java.sql.ResultSet
import java.sql.SQLException

import com.flabser.script._IObject
import com.flabser.users.User


public class Account implements _IObject {

	private long id;

	private int type;

	private String name;

	private String currencyCode;

	private BigDecimal openingBalance;

	private BigDecimal amountControl;

	private User owner;

	private String observers;

	private boolean includeInTotals;

	private String note;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(User user) {
		this.owner = user;
	}

	public String getObservers() {
		return observers;
	}

	public void setObservers(String observers) {
		this.observers = observers;
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
		return "Account[" + name + ", " + type + ", " + currencyCode + ", " + openingBalance + "]";
	}

	public void init(ResultSet rs) {
		try {
			setId(rs.getInt("id"));
			setName(rs.getString("name"));
			setType(rs.getInt("type"));
			setCurrencyCode(rs.getString("currency_code"));
			setOpeningBalance(rs.getBigDecimal("opening_balance"));
			setAmountControl(rs.getBigDecimal("amount_control"));
			setOwner(null);
			setObservers(rs.getString("observers"));
			setIncludeInTotals(rs.getBoolean("include_in_totals"));
			setNote(rs.getString("note"));
			setSortOrder(rs.getInt("sort_order"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
