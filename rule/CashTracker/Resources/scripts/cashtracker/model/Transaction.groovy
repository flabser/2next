package cashtracker.model;

import java.sql.ResultSet;
import java.util.Date;

import com.flabser.script._Exception;
import com.flabser.users.User;


public class Transaction implements com.flabser.script._IObject {

	private long id;

	private int type;

	private String author;

	private Date regDate = new Date();

	private Date date = new Date();

	private Date endDate = new Date();

	private long parentCategory;

	private long category;

	private long account;

	private BigDecimal amount;

	private long costCenter;

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

	public void setAuthor(String author) {
		this.author = author //author.getLogin();
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

	public void setCategory(long category) {
		this.category = 1 //category.getId();
	}

	public long getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(long parentCategory) {
		this.parentCategory = parentCategory;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
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

	public long getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(long costCenter) {
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
		return "Transaction[$id, $author, $regDate, $date, $endDate, $parentCategory, $category, $account, $amount, $costCenter]";
	}

	public void init(ResultSet rs){
		setId(rs.getInt("id"))
		setAuthor(null)
		setDate(rs.getDate("date"))
		setRegDate(rs.getDate("regdate"))
		//t.setCategory(rs.getLong("category"))
		//t.setParentCategory(rs.getLong("parentCategory"))
		setAmount(rs.getBigDecimal("amount"))
		setAccount(rs.getLong("account"))
		setCostCenter(rs.getLong("costcenter"))
		setRepeat(rs.getBoolean("repeat"))
		setEvery(rs.getInt("every"))
		setRepeatStep(rs.getInt("repeatstep"))
		setEndDate(rs.getDate("enddate"))
		setBasis(rs.getString("basis"))
		setComment(rs.getString("note"))
	}
}
