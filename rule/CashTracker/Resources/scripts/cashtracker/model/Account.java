package cashtracker.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.Entity;
import com.flabser.restful.data.EntityField;


@JsonRootName("account")
public class Account extends Entity {

	// private int type;
	@EntityField()
	private String name;

	@EntityField("currency_code")
	private String currencyCode;

	@EntityField("opening_balance")
	private BigDecimal openingBalance;

	@EntityField("amount_control")
	private BigDecimal amountControl;

	@EntityField()
	private boolean enabled;

	@EntityField("include_in_totals")
	private boolean includeInTotals;

	@EntityField()
	private String note;

	private List <Long> writers;

	private List <Long> readers;

	@EntityField("sort_order")
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

	public List <Long> getWriters() {
		return writers;
	}

	public void setWriters(List <Long> writers) {
		this.writers = writers;
	}

	public List <Long> getReaders() {
		return readers;
	}

	public void setReaders(List <Long> readers) {
		this.readers = readers;
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

}
