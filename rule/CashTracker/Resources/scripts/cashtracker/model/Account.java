package cashtracker.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;
import com.flabser.restful.data.EntityField;

@JsonRootName("account")
@Entity
@Table(name = "Accounts")
public class Account extends AppEntity {

	// private int type;
	@EntityField()
	private String name;

	@EntityField("currency_code")
	@Column(name = "currency_code")
	private String currencyCode;

	@EntityField("opening_balance")
	@Column(name = "opening_balance")
	private BigDecimal openingBalance;

	@EntityField("amount_control")
	@Column(name = "amount_control")
	private BigDecimal amountControl;

	@EntityField()
	private boolean enabled;

	@EntityField("include_in_totals")
	@Column(name = "include_in_totals")
	private boolean includeInTotals;

	@EntityField()
	private String note;

	@EntityField("sort_order")
	@Column(name = "sort_order")
	private int sortOrder;

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		return "Account[" + id + "," + name + ", " + currencyCode + ", " + openingBalance + "]";
	}

	public void init(ResultSet rs) throws SQLException {
		setId(rs.getLong("id"));
		setName(rs.getString("name"));
		setCurrencyCode(rs.getString("currency_code"));
		setOpeningBalance(rs.getBigDecimal("opening_balance"));
		setAmountControl(rs.getBigDecimal("amount_control"));
		setEnabled(rs.getBoolean("enabled"));

		/*
		 * Long[] _writers = (Long[]) rs.getArray("writers").getArray();
		 * setWriters(Arrays.asList(_writers));
		 * 
		 * Long[] _readers = (Long[]) rs.getArray("readers").getArray();
		 * setReaders(Arrays.asList(_readers));
		 */

		setIncludeInTotals(rs.getBoolean("include_in_totals"));
		setNote(rs.getString("note"));
		setSortOrder(rs.getInt("sort_order"));
	}

}
