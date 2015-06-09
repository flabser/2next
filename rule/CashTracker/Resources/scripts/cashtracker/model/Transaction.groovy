package cashtracker.model;

import java.util.Date;

import com.flabser.users.User;


public class Transaction {

	private long id;

	private int type;

	private String author;

	private Date regDate;

	private Date date;

	private Date endDate;

	private long parentCategory;

	private long category;

	private int account;

	private BigDecimal amount;

	private CostCenter costCenter;

	private boolean repeat;

	private int every;

	private int repeatStep;

	private String basis;

	private String comment;

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author.getLogin();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category.getId();
	}

	public long getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(long parentCategory) {
		this.parentCategory = parentCategory;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CostCenter getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(CostCenter costCenter) {
		this.costCenter = costCenter;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	@Override
	public String toString() {
		return "Transaction[" + amount + ", " + category + ", " + costCenter + "]";
	}
}
