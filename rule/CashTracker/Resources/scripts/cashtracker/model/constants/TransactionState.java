package cashtracker.model.constants;

public enum TransactionState {
	UNKNOWN(0), CONFIRMED(1), PENDING(2);

	private final int code;

	private TransactionState(int code) {
		this.code = code;
	}

	public static TransactionState stateOf(int code) {
		switch (code) {
		case 1:
			return CONFIRMED;

		case 2:
			return PENDING;

		default:
			return UNKNOWN;
		}
	}

	public int getCode() {
		return code;
	}

	public static TransactionState getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
