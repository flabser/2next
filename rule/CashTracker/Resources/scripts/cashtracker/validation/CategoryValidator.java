package cashtracker.validation;

import cashtracker.model.Category;


public class CategoryValidator {

	public CategoryValidator() {
	}

	public ValidationError validate(Category m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 64) {
			ve.addError("name", "invalid", "too_long");
		}

		if (m.getNote() != null && m.getNote().length() > 256) {
			ve.addError("note", "invalid", "too_long");
		}

		if (m.getParentId() != null) {
			if (m.getParentId().equals(m.getId())) {
				ve.addError("parent", "invalid", "cross_reference");
			} else if (m.getParent().getTransactionType() != m.getTransactionType()) {
				ve.addError("transactionTypes", "invalid", "selection_invalid");
			}
		}

		return ve;
	}
}
