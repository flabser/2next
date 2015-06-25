package cashtracker.validation

import cashtracker.model.Transaction


public class TransactionValidator {

	private boolean isValid

	public TransactionValidator() {
	}

	public boolean validate(Transaction m) {
		isValid = m.getAccountFrom() != null
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
