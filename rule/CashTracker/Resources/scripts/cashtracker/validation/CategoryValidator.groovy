package cashtracker.validation

import cashtracker.model.Category


public class CategoryValidator {

	public CategoryValidator() {
	}

	public ValidationError validate(Category m) {
		ValidationError ve = new ValidationError()

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required")
		}

		ve
	}
}
