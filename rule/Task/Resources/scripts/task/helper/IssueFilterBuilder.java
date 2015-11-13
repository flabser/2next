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
import task.model.Tag;
import task.model.constants.IssueStatus;


public class IssueFilterBuilder {

	public static IssueFilter create(String status, List <Long> tagIds, List <Long> assignees, String at) {

		IssueFilter filter = new IssueFilter();

		IssueStatus issueStatus = issueStatusFromValue(status);
		filter.setStatus(issueStatus);

		if (tagIds != null) {
			tagIds.forEach(it -> {
				Tag tag = new Tag();
				tag.setId(it);
				filter.addTag(tag);
			});
		}

		if (assignees != null) {
			filter.setAssignees(assignees);
		}

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
				filter.setMilestoneDateRange(sdate, edate);
				break;
			case "favorite":
				break;
			case "completed":
				filter.setStatus(IssueStatus.CLOSE);
				break;
			default:
				break;
			}
		}

		return filter;
	}

	private static IssueStatus issueStatusFromValue(String status) {
		if ("DRAFT".equals(status) || "PROCESS".equals(status) || "CLOSE".equals(status)) {
			return IssueStatus.valueOf(status);
		}
		return null;
	}
}
