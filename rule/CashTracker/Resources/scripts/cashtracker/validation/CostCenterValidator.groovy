package cashtracker.validation

import cashtracker.model.CostCenter


public class CostCenterValidator {

	private boolean isValid

	public CostCenterValidator() {
	}

	public boolean validate(CostCenter m) {
		isValid = !m.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
