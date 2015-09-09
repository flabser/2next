package cashtracker.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("account")
@Entity
@Table(name = "accounts")
public class Account extends AppEntity {

	@Column(nullable = false, unique = true, length = 64)
	private String name;

	@Column(name = "currency_code", nullable = false, length = 3)
	private String currencyCode;

	@Column(name = "opening_balance")
	private BigDecimal openingBalance;

	@Column(name = "amount_control")
	private BigDecimal amountControl;

	private boolean enabled;

	@Column(length = 256)
	private String note;

	private Long owner;

	@ElementCollection
	@CollectionTable(name = "account_observers", joinColumns = @JoinColumn(name = "fk_parent"))
	@Column(name = "userid")
	private List <Long> observers;

	// @OneToMany(mappedBy = "accountFrom", fetch = FetchType.LAZY)
	// private List <Transaction> transactionsFrom;

	// @OneToMany(mappedBy = "accountTo", fetch = FetchType.LAZY)
	// private List <Transaction> transactionsTo;

	//
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public List <Long> getObservers() {
		return observers;
	}

	public void setObservers(List <Long> observers) {
		this.observers = observers;
	}

	@Override
	public String toString() {
		return "Account[" + id + "," + name + ", " + currencyCode + ", " + openingBalance + ", " + getAuthor() + ", "
				+ getRegDate() + "]";
	}
}
