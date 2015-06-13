package cashtracker.model;

import java.sql.ResultSet;

import com.flabser.script._Exception;
import com.flabser.script._IObject;
import com.flabser.users.User;


public class Account implements _IObject, com.flabser.script._IContent {

	private long id;

	private String name;

	private int type;

	private String currency;

	private BigDecimal openingBalance;

	private BigDecimal amountControl;

	private String owner;

	private String observers;

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
		this.owner = user.getLogin()
	}

	public String getObservers() {
		return observers;
	}

	public void setObservers(String observers) {
		this.observers = observers;
	}

	@Override
	public String toString() {
		return "Account[" + name + ", " + type + ", " + currency + ", " + openingBalance + "]";
	}

	public void init(ResultSet rs) {
		Account m = new Account()

		m.setId(rs.getInt("id"))
		m.setName(rs.getString("name"))
		m.setAmountControl(rs.getBigDecimal("amountcontrol"))
		m.setOpeningBalance(0)
		m.setObservers(rs.getString("observers"))
		m.setOwner(null)

		m
	}

	public StringBuffer toXML() throws _Exception {
		return new StringBuffer()
	}
}
