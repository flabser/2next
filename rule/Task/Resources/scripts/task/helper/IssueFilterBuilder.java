package task.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import task.dao.filter.IssueFilter;
import task.model.constants.IssueStatus;


public class IssueFilterBuilder {

	public static IssueFilter create(String status, List <Long> tagIds, String at) {

		IssueFilter filter = new IssueFilter();

		IssueStatus issueStatus = IssueStatus.fromValue(status);
		filter.setStatus(issueStatus);

		if (at != null && !at.isEmpty()) {
			LocalDate localDate;
			switch (at) {
			case "today":
				localDate = LocalDate.now();
				Date sdate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				Date edate = Date.from(localDate.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
				filter.setMilestoneDateRange(sdate, edate);
				break;
			case "week":
				break;
			case "favorite":
				break;
			default:
				break;
			}
		}

		return filter;
	}
}
