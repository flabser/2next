package task.model.constants;

public enum IssueStatus {
	DRAFT, PROCESS, CLOSE;

	public static IssueStatus fromValue(String status) {
		if ("DRAFT".equals(status) || "PROCESS".equals(status) || "CLOSE".equals(status)) {
			return valueOf(status);
		}
		return null;
	}
}
