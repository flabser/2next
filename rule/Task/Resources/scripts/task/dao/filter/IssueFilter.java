package task.dao.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task.model.Tag;
import task.model.constants.IssueStatus;


public class IssueFilter {

	private IssueStatus status;
	private List <Tag> tags;
	private List <Long> assignees;
	private Date dueDateFrom;
	private Date dueDateUntil;

	public IssueFilter() {
		tags = new ArrayList <Tag>();
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
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

	public List <Long> getAssignees() {
		return assignees;
	}

	public void setAssignees(List <Long> assignees) {
		this.assignees = assignees;
	}

	public Date getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(Date dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public Date getDueDateUntil() {
		return dueDateUntil;
	}

	public void setDueDateUntil(Date dueDateUntil) {
		this.dueDateUntil = dueDateUntil;
	}

	public void setMilestoneDateRange(Date sd, Date ed) {
		if (sd != null && sd.compareTo(ed) >= 0) {
			throw new IllegalArgumentException("st date < end date; " + sd + " : " + ed);
		}
		this.dueDateFrom = sd;
		this.dueDateUntil = ed;
	}
}
