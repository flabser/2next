package com.flabser.scheduler.tasks;

import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.server.Server;

public class TempFileCleaner implements Job {
	private static ArrayList<String> fileToDelete = new ArrayList<String>();

	public TempFileCleaner() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.normalLogEntry("start temp file cleaner tasks");
	}

	public static void addFileToDelete(String filePath) {
		fileToDelete.add(filePath);
	}
}
