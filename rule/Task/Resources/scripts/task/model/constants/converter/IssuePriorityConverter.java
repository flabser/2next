package task.model.constants.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import task.model.constants.IssuePriority;


@Converter(autoApply = true)
public class IssuePriorityConverter implements AttributeConverter <IssuePriority, Integer> {

	@Override
	public Integer convertToDatabaseColumn(IssuePriority issuePriority) {
		return issuePriority.toValue();
	}

	@Override
	public IssuePriority convertToEntityAttribute(Integer priorityValue) {
		return IssuePriority.fromValue(priorityValue);
	}
}
