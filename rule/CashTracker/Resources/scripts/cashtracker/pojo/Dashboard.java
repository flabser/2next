package cashtracker.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("dashboard")
public class Dashboard {

	private String message;

	//
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Dashboard[" + message + "]";
	}
}
