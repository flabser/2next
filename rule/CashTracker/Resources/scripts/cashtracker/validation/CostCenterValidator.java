package cashtracker.validation;

import cashtracker.model.CostCenter;


public class CostCenterValidator {

	public CostCenterValidator() {
	}

	public ValidationError validate(CostCenter m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 128) {
			ve.addError("name", "invalid", "too_long");
		}

		return ve;
	}
}
