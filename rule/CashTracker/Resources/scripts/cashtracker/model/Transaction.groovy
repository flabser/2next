package cashtracker.model;

import java.sql.ResultSet;
import java.util.Date;

import com.flabser.script._Exception;
import com.flabser.users.User;


public class Transaction implements com.flabser.script._IObject, com.flabser.script._IContent {

	private long id;

	private int type;

	private String author;

	private Date regDate;

	private Date date;

	private Date endDate;

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

	public void setAuthor(User author) {
		this.author = "" //author.getLogin();
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
		return "Transaction[" + amount + ", " + category + ", " + costCenter + "]";
	}

	public void init(ResultSet rs){
		Transaction t = new Transaction()

		t.setId(rs.getInt("id"))
		t.setAuthor(null)
		t.setDate(rs.getDate("date"))
		t.setRegDate(rs.getDate("regdate"))
		//t.setCategory(rs.getLong("category"))
		//t.setParentCategory(rs.getLong("parentCategory"))
		t.setAmount(rs.getBigDecimal("amount"))
		t.setAccount(rs.getLong("account"))
		t.setCostCenter(rs.getLong("costcenter"))
		t.setRepeat(rs.getBoolean("repeat"))
		t.setEvery(rs.getInt("every"))
		t.setRepeatStep(rs.getInt("repeatstep"))
		t.setEndDate(rs.getDate("enddate"))
		t.setBasis(rs.getString("basis"))
		t.setComment(rs.getString("note"))

		t
	}

	public StringBuffer toXML() throws _Exception {
		StringBuffer sb = new StringBuffer()

		sb.append("""
			<transaction>
				<id>$id</id>
				<author>$author</author>
				<date>$date</date>
				<regdate>$regDate</regdate>
				<category>$category</category>
				<parentCategory>$parentCategory</parentCategory>
				<amount>$amount</amount>
				<account>$account</account>
				<costcenter>$costCenter</costcenter>
				<repeat>$repeat</repeat>
				<every>$every</every>
				<repeatstep>$repeatStep</repeatstep>
				<enddate>$endDate</enddate>
				<basis>$basis</basis>
				<note>$comment</note>
			</transaction>""")

		return sb
	}
}
