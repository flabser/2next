package cashtracker.model.constants;

public enum TransactionState {
	UNKNOWN(0), CONFIRMED(1), PENDING(2);

	private final int value;

	private TransactionState(int value) {
		this.value = value;
	}

	public static TransactionState stateOf(int value) {
		switch (value) {
		case 1:
			return CONFIRMED;

		case 2:
			return PENDING;

		default:
			return UNKNOWN;
		}
	}

	public int getCode() {
		return value;
	}
}
