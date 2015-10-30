package task.validation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("errors")
public class ValidationError {

	private List <Error> errors = new ArrayList <Error>();

	public void addError(String field, String type, String msg) {
		errors.add(new Error(field, type, msg));
	}

	public List <Error> getErrors() {
		return errors;
	}

	public boolean hasError() {
		return errors.size() > 0;
	}

	public class Error {

		String field;
		String type;
		String message;

		public Error(String field, String type, String message) {
			this.field = field;
			this.type = type;
			this.message = message;
		}

		public String getField() {
			return field;
		}

		public String getType() {
			return type;
		}

		public String getMessage() {
			return message;
		}

		public String toString() {
			return field + ", " + type + ", " + message;
		}
	}
}
