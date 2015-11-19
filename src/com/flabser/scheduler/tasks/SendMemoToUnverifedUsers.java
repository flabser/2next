package com.flabser.scheduler.tasks;

import java.util.ArrayList;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.users.User;

//TODO To realize
public class SendMemoToUnverifedUsers implements Job {

	public SendMemoToUnverifedUsers() {

	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
		ArrayList<User> users = sysDb.getAllUsers("status=460", 0, 100);
		for (User user : users) {
			System.out.println(user);
		}

	}
}
