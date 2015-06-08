package cashtracker.model;

import java.math.BigDecimal;

import com.flabser.users.User;


public class Account {

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
		this.owner = user.getUserID();
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
}
