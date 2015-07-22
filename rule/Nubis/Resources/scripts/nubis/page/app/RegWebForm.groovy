package nubis.page.app

import com.flabser.script.*


class RegWebForm {

	String email
	String pwd
	private boolean valid = false
	def errors = []

	public RegWebForm(_WebFormData formData) {
		email = formData.getValueSilently("email")
		pwd = formData.getValueSilently("pwd")

		validate()
	}

	private void validate() {
		boolean _valid = true

		if (!_Validator.checkEmail(email)) {
			_valid = false
			errors << "email"
		}

		if (pwd.length() < 8) {
			_valid = false
			errors << "pwd-weak"
		} else {
			def rgxp = [/[0-9]/, /[a-zA-Zа-яА-Я]/]
			def res = rgxp.findAll {
				return (pwd =~ it).count > 0
			}

			if (res.size < 2) {
				_valid = false
				errors << "pwd-weak"
			}
		}

		valid = _valid
	}

	boolean isValid() {
		valid
	}
}
