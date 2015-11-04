package task.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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

import task.serializers.JsonDateSerializer;


@JsonRootName("issue")
@Entity
@Table(name = "issues")
@NamedQuery(name = "Issue.findAll", query = "SELECT t FROM Issue AS t ORDER BY t.milestone")
public class Issue extends AppEntity {

	public enum Status {
		DRAFT, PROCESS, CLOSE;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task")
	private Task task;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private Issue parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List <Issue> children;

	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private Long executor;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date milestone;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category")
	private Category category;

	@Column(length = 2000)
	private String body;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Issue[" + id + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
