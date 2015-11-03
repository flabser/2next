package task.model.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task.model.Category;


public class IssueFilter {

	private List <Category> categories;
	private Date[] milestoneDateRange = new Date[2];

	public IssueFilter() {
		categories = new ArrayList <Category>();
	}

	public List <Category> getCategories() {
		return categories;
	}

	public void setCategories(List <Category> categories) {
		this.categories = categories;
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
