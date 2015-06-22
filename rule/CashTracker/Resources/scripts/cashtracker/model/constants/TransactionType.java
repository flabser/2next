package cashtracker.model.constants;

public enum TransactionType {
	UNKNOWN(0), EXPENSE(1), INCOME(2), TRANSFER(3);

	private final int value;

	private TransactionType(int value) {
		this.value = value;
	}

	public static TransactionType typeOf(int value) {
		switch (value) {
		case 1:
			return EXPENSE;

		case 2:
			return INCOME;

		case 3:
			return TRANSFER;

		default:
			return UNKNOWN;
		}
	}

	public int getCode() {
		return value;
	}
}
