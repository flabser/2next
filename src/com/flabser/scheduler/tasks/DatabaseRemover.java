package com.flabser.scheduler.tasks;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.server.Server;

//TODO To realize
public class DatabaseRemover implements Job {

	public DatabaseRemover() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.normalLogEntry("start database remover task");
	}
}
