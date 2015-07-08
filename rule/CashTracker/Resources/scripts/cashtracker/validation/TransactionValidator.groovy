package cashtracker.validation

import cashtracker.model.Transaction


public class TransactionValidator {

	public TransactionValidator() {
	}

	public ValidationError validate(Transaction m) {
		ValidationError ve = new ValidationError()

		if (m.amount == null || m.amount == 0) {
			ve.addError("amount", "required", "amount_required")
		}

		ve
	}
}
