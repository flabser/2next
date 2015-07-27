package com.flabser.server;

import java.util.Scanner;

public class Console implements Runnable {

	Console() {

	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String command = scanner.nextLine();
		System.out.println(command);
		if (command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("q")) {
			Server.shutdown();
		}

	}
}
