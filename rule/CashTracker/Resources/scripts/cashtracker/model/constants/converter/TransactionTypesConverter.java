package cashtracker.model.constants.converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import cashtracker.model.constants.TransactionType;


@Converter(autoApply = true)
public class TransactionTypesConverter implements AttributeConverter <List <TransactionType>, String[]> {

	@Override
	public List <TransactionType> convertToEntityAttribute(String[] values) {
		if (values == null) {
			return null;
		}

		List <TransactionType> result = Arrays.stream(values).map(TransactionType::typeOf).collect(Collectors.toList());
		return result;
	}

	@Override
	public String[] convertToDatabaseColumn(List <TransactionType> types) {
		if (types == null) {
			return new String[] {};
		}

		String[] result = types.stream().map(TransactionType::toValue).toArray(String[]::new);
		return result;
	}
}
