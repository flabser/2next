package task.dao.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task.model.Category;
import task.model.Issue.Status;


public class IssueFilter {

	private Status status;
	private List <Category> categories;
	private Date[] milestoneDateRange = new Date[2];

	public IssueFilter() {
		categories = new ArrayList <Category>();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List <Category> getCategories() {
		return categories;
	}

	public void setCategories(List <Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	public Date[] getMilestoneDateRange() {
		return milestoneDateRange;
	}

	public void setMilestoneDateRange(Date sd, Date ed) {
		if (sd.compareTo(ed) >= 0) {
			throw new IllegalArgumentException("st date < end date; " + sd + " : " + ed);
		}
		this.milestoneDateRange = new Date[] { sd, ed };
	}
}
