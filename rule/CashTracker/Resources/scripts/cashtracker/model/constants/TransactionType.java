package cashtracker.model.constants;

public enum TransactionType {
	EXPENSE("E"), INCOME("I"), TRANSFER("T");

	private final String value;

	private TransactionType(String value) {
		this.value = value;
	}

	public static TransactionType typeOf(String value) {
		if (value != null) {
			for (TransactionType type : values()) {
				if (type.value.equals(value)) {
					return type;
				}
			}
		}

		throw new IllegalArgumentException("Unknown value: " + value);
	}

	public String toValue() {
		return value;
	}
}
