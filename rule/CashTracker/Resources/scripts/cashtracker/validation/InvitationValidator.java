package cashtracker.validation;


import com.flabser.dataengine.system.entities.Invitation;
import com.flabser.script._Validator;


public class InvitationValidator {

	public InvitationValidator() {
	}

	public ValidationError validate(Invitation m) {
		ValidationError ve = new ValidationError();

		if (m.getEmail() == null || m.getEmail().isEmpty()) {
			ve.addError("email", "required", "required");
		} else if (!_Validator.checkEmail(m.getEmail())) {
			ve.addError("email", "invalid", "invalid");
		}

		if (m.getMessage() == null || m.getMessage().isEmpty()) {
			ve.addError("message", "required", "required");
		}

		return ve;
	}
}
