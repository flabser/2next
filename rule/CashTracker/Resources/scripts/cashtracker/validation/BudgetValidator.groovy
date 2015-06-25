package cashtracker.validation

import cashtracker.model.Budget


public class BudgetValidator {

	private boolean isValid

	public BudgetValidator() {
	}

	public boolean validate(Budget m) {
		isValid = !m.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
