package cashtracker.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import cashtracker.model.constants.TransactionState;


@Converter(autoApply = true)
public class TransactionStateConverter implements AttributeConverter <TransactionState, String> {

	@Override
	public TransactionState convertToEntityAttribute(String dbValue) {
		return TransactionState.stateOf(dbValue);
	}

	@Override
	public String convertToDatabaseColumn(TransactionState type) {
		if (type == null) {
			return null;
		}
		return type.toValue();
	}
}
