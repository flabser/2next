package nubis.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.dataengine.jpa.SecureAppEntity;


@JsonRootName("workspace")
@Entity
@Table(name = "workspace")
public class Workspace extends SecureAppEntity {

	@JsonIgnore
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "icons", joinColumns = { @JoinColumn(name = "icon_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "icon_id", referencedColumnName = "id") })
	private List <Icon> icons;


	@Override
	public String toString() {
		return "Workspace[" + id + ",]";
	}
}
