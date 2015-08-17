package cashtracker.model.constants;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter(autoApply = true)
public class TransactionTypesConverter implements AttributeConverter <List <TransactionType>, String[]> {

	@Override
	public List <TransactionType> convertToEntityAttribute(String[] types) {
		if (types == null) {
			return null;
		}

		List <TransactionType> tt = new ArrayList <TransactionType>();
		for (String name : types) {
			tt.add(TransactionType.typeOf(name));
		}
		return tt;
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
