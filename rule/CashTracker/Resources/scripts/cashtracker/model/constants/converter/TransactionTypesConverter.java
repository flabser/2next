package cashtracker.model.constants.converter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import cashtracker.model.constants.TransactionType;


@Converter(autoApply = true)
public class TransactionTypesConverter implements AttributeConverter <List <TransactionType>, String[]> {

	@Override
	public List <TransactionType> convertToEntityAttribute(String[] types) {
		if (types == null) {
			return null;
		}

		List <TransactionType> result = new ArrayList <TransactionType>();
		for (String name : types) {
			result.add(TransactionType.typeOf(name));
		}
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
