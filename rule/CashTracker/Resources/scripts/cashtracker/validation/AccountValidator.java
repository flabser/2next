package cashtracker.validation;

import cashtracker.model.Account;


public class AccountValidator {

	public AccountValidator() {
	}

	public ValidationError validate(Account m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		}

		return ve;
	}
}
