package task.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flabser.dataengine.jpa.AppEntity;

import task.model.constants.IssuePriority;
import task.model.constants.IssueStatus;
import task.model.constants.converter.IssuePriorityConverter;
import task.serializers.JsonDateSerializer;


@JsonRootName("issue")
@Entity
@Table(name = "issues")
@NamedQuery(name = "Issue.findAll", query = "SELECT m FROM Issue AS m ORDER BY m.dueDate")
public class Issue extends AppEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Issue parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List <Issue> children;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 16)
	private IssueStatus status = IssueStatus.DRAFT;

	@Convert(converter = IssuePriorityConverter.class)
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private IssuePriority priority = IssuePriority.NORMAL;

	private Long assignee;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "issue_tags", joinColumns = {
			@JoinColumn(name = "issue_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "tag_id", referencedColumnName = "id") })
	private List <Tag> tags;

	@Column(length = 2048)
	private String body;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "issue_comments", joinColumns = {
			@JoinColumn(name = "issue_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "comment_id", referencedColumnName = "id") })
	private List <Comment> comments;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "issue_attachments", joinColumns = {
			@JoinColumn(name = "issue_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "attachment_id", referencedColumnName = "id") })
	private List <Attachment> attachments;

	public Issue getParent() {
		return parent;
	}

	public void setParent(Issue parent) {
		this.parent = parent;
	}

	public List <Issue> getChildren() {
		return children;
	}

	public void setChildren(List <Issue> children) {
		this.children = children;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
	}

	public IssuePriority getPriority() {
		return priority;
	}

	public void setPriority(IssuePriority priority) {
		this.priority = priority;
	}

	@JsonGetter("priority")
	public int getPriorityOrdinal() {
		return priority.toValue();
	}

	@JsonSetter("priority")
	public void setPriority(int priorityOrdinal) {
		this.priority = IssuePriority.fromValue(priorityOrdinal);
	}

	public Long getAssignee() {
		return assignee;
	}

	public void setAssignee(Long assignee) {
		this.assignee = assignee;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
	}

	@JsonSetter("tags")
	public void setTagsId(List <Long> ids) {
		if (ids != null) {
			List <Tag> _tags = new ArrayList <Tag>();
			ids.forEach(id -> {
				Tag tag = new Tag();
				tag.setId(id);
				_tags.add(tag);
			});
			setTags(_tags);
		} else {
			setTags(null);
		}
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List <Comment> getComments() {
		return comments;
	}

	public void setComments(List <Comment> comments) {
		this.comments = comments;
	}

	public List <Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List <Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "Issue[" + id + ", " + status + ", " + assignee + ", " + startDate + ", " + dueDate + ", " + body
				+ ", parent:" + parent + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
