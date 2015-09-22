package cashtracker.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("invitation")
public class Invitation {

	private String email;

	private String message;

	//
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Invitation[" + email + ", " + message + "]";
	}
}
