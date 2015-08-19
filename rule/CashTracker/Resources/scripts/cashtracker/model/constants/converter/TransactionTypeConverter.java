package cashtracker.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import cashtracker.model.constants.TransactionType;


@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter <TransactionType, String> {

	@Override
	public TransactionType convertToEntityAttribute(String dbValue) {
		return TransactionType.typeOf(dbValue);
	}

	@Override
	public String convertToDatabaseColumn(TransactionType type) {
		return type.toValue();
	}
}
