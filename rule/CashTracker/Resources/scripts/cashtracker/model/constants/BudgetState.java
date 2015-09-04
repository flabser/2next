package cashtracker.model.constants;

public enum BudgetState {
	ACTIVE("A"), DELETED("D");

	private final String value;

	private BudgetState(String value) {
		this.value = value;
	}

	public static BudgetState stateOf(String value) {
		if (value != null) {
			for (BudgetState state : values()) {
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

	public static BudgetState getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
