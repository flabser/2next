package cashtracker.validation

import cashtracker.model.Category


public class CategoryValidator {

	private boolean isValid

	public CategoryValidator() {
	}

	public boolean validate(Category m) {
		isValid = !m.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
