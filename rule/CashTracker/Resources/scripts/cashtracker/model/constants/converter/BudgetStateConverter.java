package cashtracker.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import cashtracker.model.constants.BudgetState;


@Converter(autoApply = true)
public class BudgetStateConverter implements AttributeConverter <BudgetState, String> {

	@Override
	public BudgetState convertToEntityAttribute(String code) {
		return BudgetState.stateOf(code);
	}

	@Override
	public String convertToDatabaseColumn(BudgetState state) {
		if (state == null) {
			return null;
		}
		return state.toValue();
	}
}
