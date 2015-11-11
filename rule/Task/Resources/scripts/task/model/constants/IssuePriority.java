package task.model.constants;

public enum IssuePriority {
	HEIGHEST(1), HEIGHT(2), MEDIUM(3), NORMAL(4);

	private final int value;

	private IssuePriority(int value) {
		this.value = value;
	}

	public static IssuePriority fromValue(int priority) {
		switch (priority) {
		case 1:
			return HEIGHEST;
		case 2:
			return HEIGHT;
		case 3:
			return MEDIUM;
		case 4:
			return NORMAL;
		}

		return null;
	}

	public int toValue() {
		return value;
	}
}
