package com.flabser.scheduler.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.server.Server;

public class TempFileCleaner implements Job {

	public TempFileCleaner() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.normalLogEntry("start temp file cleaner tasks");
	}
}
