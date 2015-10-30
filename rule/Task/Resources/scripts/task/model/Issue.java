package task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("issue")
@Entity
@Table(name = "issues")
@NamedQuery(name = "Issue.findAll", query = "SELECT t FROM Issue AS t ORDER BY t.deadline")
public class Issue extends AppEntity {

	/*@Column(nullable = false)
	private Task task;*/

	@Column(nullable = false)
	private Long executor;

	@Column(nullable = false)
	private Date deadline;

	@Column(nullable = false, length = 1000)
	private String note;

	//
	/*public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}*/

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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "Issue[" + id + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
