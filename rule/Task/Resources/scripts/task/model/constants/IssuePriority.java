package task.model.constants;

public enum IssuePriority {
	HEIGHEST, HEIGHT, MEDIUM, NORMAL;

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
}
