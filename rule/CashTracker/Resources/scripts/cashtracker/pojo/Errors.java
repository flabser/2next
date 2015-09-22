package cashtracker.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("errors")
public class Errors {

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
		return "Errors[" + message + "]";
	}
}
