package cashtracker.validation;

import cashtracker.model.Category;
import cashtracker.model.Transaction;
import cashtracker.model.constants.TransactionType;


public class TransactionValidator {

	public TransactionValidator() {
	}

	public ValidationError validate(Transaction m) {
		ValidationError ve = new ValidationError();

		TransactionType tt = m.getTransactionType();
		if (tt == null) {
			ve.addError("transactionType", "required", "required");
		} else if (tt == TransactionType.TRANSFER && m.getAccountTo() == null) {
			ve.addError("accountTo", "required", "required");
		}

		if (m.getAmount() == null || m.getAmount().intValue() == 0) {
			ve.addError("amount", "required", "required");
		}

		if (m.getNote() != null && m.getNote().length() > 256) {
			ve.addError("note", "invalid", "too_long");
		}

		if (m.getAccountFrom() == null) {
			ve.addError("accountFrom", "required", "required");
		}

		if (!m.getAccountFrom().isEnabled()) {
			ve.addError("accountFrom", "invalid", "disabled");
		}

		if (m.getAccountTo() != null && !m.getAccountTo().isEnabled()) {
			ve.addError("accountTo", "invalid", "disabled");
		}

		Category c = m.getCategory();
		if (c != null && !ve.hasError()) {
			if (!c.getTransactionTypes().contains(tt)) {
				ve.addError("category", "invalid", "selection_error");
			}
		}

		return ve;
	}
}
