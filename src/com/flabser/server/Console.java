package com.flabser.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import com.flabser.dataengine.DatabaseFactory;
import com.flabser.dataengine.system.ISystemDatabase;
import com.flabser.dataengine.system.entities.ApplicationProfile;
import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.scheduler.PeriodicalServices;
import com.flabser.scheduler.tasks.DatabaseRemover;
import com.flabser.users.ApplicationStatusType;
import com.flabser.users.User;
import com.flabser.users.UserSession;
import com.flabser.util.Util;

public class Console implements Runnable {

	@Override
	public void run() {

		final Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			final String command = in.nextLine();
			System.out.println("> " + command);
			if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q") || command.equalsIgnoreCase("exit")) {
				Server.shutdown();
				in.close();
			} else if (command.equalsIgnoreCase("sessions") || command.equalsIgnoreCase("us")) {
				Collection<UserSession> sc = SessionPool.getUserSessions().values();
				if (sc.size() > 0) {
					for (UserSession us : SessionPool.getUserSessions().values()) {
						System.out.println(us);
					}
				} else {
					System.out.println("No user session");
				}
			} else if (command.equalsIgnoreCase("job list") || command.equalsIgnoreCase("jl")) {
				PeriodicalServices services = Environment.periodicalServices;

				System.out.println(services.getCurrentJobs());
			} else if (command.contains("force")) {
				if (command.contains("database remover")) {
					new DatabaseRemover().process(ApplicationStatusType.DATABASE_NOT_CREATED);
				}
			} else if (command.contains("show") || command.contains("sh")) {
				if (command.contains("applications") || command.contains("apps")) {
					ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
					ArrayList<ApplicationProfile> apps = sysDb.getAllApps("", 0, 0);
					for (ApplicationProfile ap : apps) {
						System.out.println(ap);
					}
				} else if (command.contains("users") || command.contains("us")) {
					ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
					ArrayList<User> users = sysDb.getAllUsers("", 0, 0);
					for (User u : users) {
						System.out.println(u);
					}
				}
			} else if (command.contains("delete") || command.contains("del")) {
				if (command.contains("application") || command.contains("app")) {
					ISystemDatabase sysDb = DatabaseFactory.getSysDatabase();
					if (command.contains("all")) {
						ArrayList<ApplicationProfile> apps = sysDb.getAllApps("", 0, 0);
						int count = 0;
						for (ApplicationProfile ap : apps) {
							String app = ap.toString();
							int stat = sysDb.deleteApplicationProfile(ap.id);
							if (stat == 0) {
								System.out.println(app + " has been deleted");
								count++;
							} else {
								System.out.println(app + " has not been deleted");
							}
						}
						System.out.println("was deleted " + count + " applications");
					} else {
						System.err.println("command \"" + command + "\" is not recognized");
					}
				} else {
					System.err.println("command \"" + command + "\" is not recognized");
				}
			} else if (command.contains("command")) {
				if (command.contains("898")) {
					new DatabaseRemover().process(ApplicationStatusType.DATABASE_NOT_CREATED);
				} else if (command.contains("899")) {
					new DatabaseRemover().process(ApplicationStatusType.READY_TO_DEPLOY);
				}
			} else if (command.equals("help") || command.equalsIgnoreCase("h")) {
				System.out.println(Util.readFile("resources" + File.separator + "console_commands.txt"));
			} else {
				if (!command.trim().equalsIgnoreCase("")) {
					System.err.println("command \"" + command + "\" is not recognized");
				}
			}
		}
	}
}
