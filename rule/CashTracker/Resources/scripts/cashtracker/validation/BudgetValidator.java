package cashtracker.validation;

import cashtracker.model.Budget;


public class BudgetValidator {

	public BudgetValidator() {
	}

	public ValidationError validate(Budget m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		}

		return ve;
	}
}
