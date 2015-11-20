package task.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Comment[" + id + ", " + comment + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
