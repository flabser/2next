package task.helper;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

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
			LocalDate today = LocalDate.now();
			Date sdate;
			Date edate;
			switch (at) {
			case "today":
				sdate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
				edate = Date.from(today.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
				filter.setMilestoneDateRange(sdate, edate);
				break;
			case "week":
				LocalDate monday = today.with(previousOrSame(MONDAY));
				LocalDate sunday = today.with(nextOrSame(SUNDAY));
				sdate = Date.from(monday.atStartOfDay(ZoneId.systemDefault()).toInstant());
				edate = Date.from(sunday.plusDays(1L).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
