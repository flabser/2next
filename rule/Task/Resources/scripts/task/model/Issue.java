package task.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flabser.dataengine.jpa.AppEntity;

import task.model.constants.IssuePriority;
import task.model.constants.IssueStatus;
import task.serializers.JsonDateSerializer;


@JsonRootName("issue")
@Entity
@Table(name = "issues")
@NamedQuery(name = "Issue.findAll", query = "SELECT t FROM Issue AS t ORDER BY t.milestone")
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

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	private IssuePriority priority = IssuePriority.NORMAL;

	@Column(nullable = false)
	private Long executor;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date milestone;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "issue_tags", joinColumns = {
			@JoinColumn(name = "issue_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "tag_id", referencedColumnName = "id") })
	private List <Tag> tags;

	@Column(length = 2000)
	private String body;

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

	public Long getExecutor() {
		return executor;
	}

	public void setExecutor(Long executor) {
		this.executor = executor;
	}

	public Date getMilestone() {
		return milestone;
	}

	public void setMilestone(Date milestone) {
		this.milestone = milestone;
	}

	public List <Tag> getTags() {
		return tags;
	}

	public void setTags(List <Tag> tags) {
		this.tags = tags;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Issue[" + id + ", " + status + ", " + executor + ", " + milestone + ", " + body + ", parent:" + parent
				+ ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
