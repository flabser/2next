package task.dao.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task.model.Issue.Status;
import task.model.Tag;


public class IssueFilter {

	private Status status;
	private List <Tag> tags;
	private Date[] milestoneDateRange = new Date[2];

	public IssueFilter() {
		tags = new ArrayList <Tag>();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
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
