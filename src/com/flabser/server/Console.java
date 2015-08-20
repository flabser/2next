package com.flabser.server;

import java.util.Collection;
import java.util.Scanner;

import com.flabser.env.Environment;
import com.flabser.env.SessionPool;
import com.flabser.scheduler.PeriodicalServices;
import com.flabser.users.UserSession;

public class Console implements Runnable {

	@Override
	public void run() {

		final Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			final String command = in.nextLine();
			System.out.println("> " + command);
			if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q")) {
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

			} else {
				if (!command.trim().equalsIgnoreCase("")) {
					System.err.println("command \"" + command + "\" is not recognized");
				}
			}
		}
	}
}
