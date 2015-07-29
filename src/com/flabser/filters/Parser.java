package com.flabser.filters;

public class Parser {
	public static void main(String[] args) {
		System.out.println("start");
		String v = "/CashTracker/zu2l161ce0cfi00b/index.html";
		String g = v.substring(1, v.length());
		String appType = g.substring(0, g.indexOf("/"));
		String appID = g.substring(appType.length() + 1, g.indexOf("/index.html"));
		System.out.println("result: " + appType + " " + appID);

	}
}
