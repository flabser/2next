package com.flabser.scheduler.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.server.Server;

//TODO To realize
public class SendMemoToUnverifedUsers implements Job {

	public SendMemoToUnverifedUsers() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.normalLogEntry("start sending process to unverifed users");

	}
}
