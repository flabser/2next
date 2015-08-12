package cashtracker.validation;

import cashtracker.model.Transaction;


public class TransactionValidator {

	public TransactionValidator() {
	}

	public ValidationError validate(Transaction m) {
		ValidationError ve = new ValidationError();

		if (m.getAmount() == null || m.getAmount().intValue() == 0) {
			ve.addError("amount", "required", "required");
		}

		return ve;
	}
}
