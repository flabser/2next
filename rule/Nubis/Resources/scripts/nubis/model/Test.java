package nubis.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.data.SecureAppEntity;


@JsonRootName("test")
@Entity
@Table(name = "tests")
public class Test extends SecureAppEntity {

	@JsonIgnore
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Date birthDay;

	private String sex;

	private String country;

	private String city;



	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public Date getBirthDay() {
		return birthDay;
	}



	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	@Override
	public String toString() {
		return "Profile[" + id + ",]";
	}
}
