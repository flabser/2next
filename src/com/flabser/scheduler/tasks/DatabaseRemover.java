package com.flabser.scheduler.tasks;

import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.server.Server;

//TODO To realize
public class DatabaseRemover implements Job {

	public DatabaseRemover() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Server.logger.normalLogEntry("start database remover task");
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		ArrayList<ApplicationProfile> apps = sysDb.getAllApps("", 0, 100);
		for (ApplicationProfile ap : apps) {

		}
	}
}
