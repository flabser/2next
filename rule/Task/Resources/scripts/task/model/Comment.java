package task.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("comment")
@Entity
@Table(name = "comments")
@NamedQuery(name = "Comment.findAll", query = "SELECT m FROM Comment AS m ORDER BY m.regDate ASC")
public class Comment extends AppEntity {

	@Column(nullable = false, length = 512)
	private String comment;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "comment_attachments", joinColumns = {
			@JoinColumn(name = "comment_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "attachment_id", referencedColumnName = "id") })
	private List <Attachment> attachments;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List <Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List <Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "Comment[" + id + ", " + comment + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
