package cashtracker.model.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class BudgetStateConverter implements AttributeConverter <BudgetState, String> {

	@Override
	public BudgetState convertToEntityAttribute(String code) {
		return BudgetState.stateOf(code);
	}

	@Override
	public String convertToDatabaseColumn(BudgetState type) {
		return type.toValue();
	}
}
