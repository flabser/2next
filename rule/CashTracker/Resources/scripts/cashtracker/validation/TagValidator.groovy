package cashtracker.validation

import cashtracker.model.Tag


public class TagValidator {

	private boolean isValid

	public TagValidator() {
	}

	public boolean validate(Tag m) {
		isValid = !m.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
