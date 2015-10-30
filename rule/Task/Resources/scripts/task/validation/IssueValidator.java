package task.validation;

import task.model.Issue;


public class IssueValidator {

	public IssueValidator() {
	}

	public ValidationError validate(Issue m) {
		ValidationError ve = new ValidationError();

		if (m.getNote() == null || m.getNote().isEmpty()) {
			ve.addError("note", "required", "required");
		} else if (m.getNote().length() > 1000) {
			ve.addError("note", "invalid", "too_long");
		}

		return ve;
	}
}
