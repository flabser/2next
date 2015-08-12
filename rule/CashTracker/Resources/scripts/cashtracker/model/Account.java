package cashtracker.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.Entity;
import com.flabser.restful.data.EntitySetterField;

@JsonRootName("account")
public class Account extends Entity {

	// private int type;

	private String name;

	private String currencyCode;

	private BigDecimal openingBalance;

	private BigDecimal amountControl;

	private boolean enabled;

	private boolean includeInTotals;

	private String note;

	private List<Long> writers;

	private List<Long> readers;

	private int sortOrder;

	public String getName() {
		return name;
	}

	@EntitySetterField()
	public void setName(String name) {
		this.name = name;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	@EntitySetterField("currency_code")
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	@EntitySetterField("opening_balance")
	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getAmountControl() {
		return amountControl;
	}

	@EntitySetterField("amount_control")
	public void setAmountControl(BigDecimal amountControl) {
		this.amountControl = amountControl;
	}

	public List<Long> getWriters() {
		return writers;
	}

	public void setWriters(List<Long> writers) {
		this.writers = writers;
	}

	public List<Long> getReaders() {
		return readers;
	}

	public void setReaders(List<Long> readers) {
		this.readers = readers;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@EntitySetterField()
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isIncludeInTotals() {
		return includeInTotals;
	}

	@EntitySetterField("include_in_totals")
	public void setIncludeInTotals(boolean includeInTotals) {
		this.includeInTotals = includeInTotals;
	}

	public String getNote() {
		return note;
	}

	@EntitySetterField()
	public void setNote(String note) {
		this.note = note;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	@EntitySetterField("sort_order")
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "Account[" + name + ", " + currencyCode + ", " + openingBalance + "]";
	}

	@Override
	public void init(ResultSet rs) throws SQLException {
		setId(rs.getLong("id"));
		setName(rs.getString("name"));
		setCurrencyCode(rs.getString("currency_code"));
		setOpeningBalance(rs.getBigDecimal("opening_balance"));
		setAmountControl(rs.getBigDecimal("amount_control"));
		setEnabled(rs.getBoolean("enabled"));

		Long[] _writers = (Long[]) rs.getArray("writers").getArray();
		setWriters(Arrays.asList(_writers));

		Long[] _readers = (Long[]) rs.getArray("readers").getArray();
		setReaders(Arrays.asList(_readers));

		setIncludeInTotals(rs.getBoolean("include_in_totals"));
		setNote(rs.getString("note"));
		setSortOrder(rs.getInt("sort_order"));
	}

	@Override
	@JsonIgnore
	public String getTableName() {
		return "accounts";
	}
}
