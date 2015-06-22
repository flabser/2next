package cashtracker.validation

import cashtracker.model.Account;


public class AccountValidator {

	private boolean isValid

	public AccountValidator() {
	}

	public boolean validate(Account account) {
		isValid = !account.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
