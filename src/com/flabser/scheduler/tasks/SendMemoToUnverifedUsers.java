package com.flabser.scheduler.tasks;

import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.IUser;

//TODO To realize
public class SendMemoToUnverifedUsers implements Job {

	public SendMemoToUnverifedUsers() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		ArrayList<IUser> users = sysDb.getAllUsers("status=460", 0, 100);
		for (IUser user : users) {

		}

	}
}
