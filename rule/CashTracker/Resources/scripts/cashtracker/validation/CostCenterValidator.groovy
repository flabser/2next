package cashtracker.validation

import cashtracker.model.CostCenter


public class CostCenterValidator {

	public CostCenterValidator() {
	}

	public ValidationError validate(CostCenter m) {
		ValidationError ve = new ValidationError()

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "name_required")
		}

		ve
	}
}
