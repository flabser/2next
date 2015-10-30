package task.validation;

import task.model.Task;


public class TaskValidator {

	public TaskValidator() {
	}

	public ValidationError validate(Task m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 256) {
			ve.addError("name", "invalid", "too_long");
		}

		return ve;
	}
}
