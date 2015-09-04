package cashtracker.model.constants;

public enum TransactionState {
	CONFIRMED("C"), PENDING("P");

	private final String value;

	private TransactionState(String value) {
		this.value = value;
	}

	public static TransactionState stateOf(String value) {
		if (value != null) {
			for (TransactionState state : values()) {
				if (state.value.equals(value)) {
					return state;
				}
			}
		}

		return null;
	}

	public String toValue() {
		return value;
	}

	public static TransactionState getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
