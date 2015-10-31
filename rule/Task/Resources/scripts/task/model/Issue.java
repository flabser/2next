package task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("issue")
@Entity
@Table(name = "issues")
@NamedQuery(name = "Issue.findAll", query = "SELECT t FROM Issue AS t ORDER BY t.deadline")
public class Issue extends AppEntity {

	public enum Status {
		DRAFT, PROCESS, CLOSE;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task")
	private Task task;

	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private Long executor;

	@Column(nullable = false)
	private Date deadline;

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

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
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
