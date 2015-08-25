package nubis.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.AppEntity;


@JsonRootName("profile")
@Entity
@Table(name = "profiles")
public class Profile extends AppEntity {

	@JsonIgnore
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Date birthDay;

	private String sex;

	private String country;

	private String city;



	@Override
	public String toString() {
		return "Profile[" + id + ",]";
	}
}
