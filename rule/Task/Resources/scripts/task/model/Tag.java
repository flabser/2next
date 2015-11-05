package task.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.flabser.dataengine.jpa.AppEntity;


@JsonRootName("tag")
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = { "parent", "name" }) )
@NamedQuery(name = "Tag.findAll", query = "SELECT m FROM Tag AS m WHERE m.parent IS NULL ORDER BY m.name")
public class Tag extends AppEntity {

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private Tag parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List <Tag> children;

	@Column(nullable = false, length = 64)
	private String name;

	public List <Tag> getChildren() {
		return children;
	}

	public Tag getParent() {
		return parent;
	}

	public void setParent(Tag parent) {
		this.parent = parent;
	}

	@JsonGetter("parent")
	public Long getParentId() {
		if (parent == null || parent.id < 1) {
			return null;
		}
		return parent.id;
	}

	@JsonSetter("parent")
	public void setParentId(Long id) {
		if (id == null || id < 1) {
			setParent(null);
			return;
		}

		Tag parent = new Tag();
		parent.setId(id);
		setParent(parent);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag[" + id + ", " + name + ", " + parent + ", " + getAuthor() + ", " + getRegDate() + "]";
	}
}
