package cashtracker.model.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


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
