package cashtracker.validation;

import cashtracker.model.Category;


public class CategoryValidator {

	public CategoryValidator() {
	}

	public ValidationError validate(Category m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 128) {
			ve.addError("name", "invalid", "too_long");
		}

		return ve;
	}
}
