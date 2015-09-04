package cashtracker.validation;

import cashtracker.model.Budget;


public class BudgetValidator {

	public BudgetValidator() {
	}

	public ValidationError validate(Budget m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 64) {
			ve.addError("name", "invalid", "too_long");
		}

		return ve;
	}
}
