package task.validation;

import task.model.Issue;


public class IssueValidator {

	public IssueValidator() {
	}

	public ValidationError validate(Issue m) {
		ValidationError ve = new ValidationError();

		if (m.getBody() == null || m.getBody().isEmpty()) {
			ve.addError("note", "required", "required");
		} else if (m.getBody().length() > 2000) {
			ve.addError("note", "invalid", "too_long");
		}

		return ve;
	}
}
