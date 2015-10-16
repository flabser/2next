package nubis.page.app;

import java.util.ArrayList;

import com.flabser.script._Validator;
import com.flabser.script._WebFormData;

public class RegWebForm {

	public String email;
	public String pwd;
	public String userName;
	private boolean valid = false;
	private ArrayList<String> errors = new ArrayList<String>();

	public RegWebForm(_WebFormData formData) {
		email = formData.getValueSilently("email");
		pwd = formData.getValueSilently("pwd");

		validate();
	}

	private void validate() {
		boolean _valid = true;

		if (!_Validator.checkEmail(email)) {
			_valid = false;
			errors.add("email");
		}

		if (!_Validator.checkPwdWeakness(pwd, 8)) {
			_valid = false;
			errors.add("pwd-weak");
		}

		valid = _valid;
	}

	public boolean isValid() {
		return valid;
	}

	public ArrayList<String> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<String> errors) {
		this.errors = errors;
	}
}
