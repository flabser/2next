package task.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import task.serializers.JsonDateSerializer;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("task")
@Entity
@Table(name = "tasks")
@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task AS t ORDER BY t.deadline desc")
public class Task extends AppEntity {

	@Column(nullable = false, length = 256)
	private String name;

	@JsonSerialize(using = JsonDateSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date deadline;

	@OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List <Issue> issues;

	public List <Issue> getIssues() {
		return issues;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "Task[" + id + ", " + name + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
