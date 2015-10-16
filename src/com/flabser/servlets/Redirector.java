package com.flabser.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Redirector extends HttpServlet {
	private static final long serialVersionUID = 6569087969707400188L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			response.sendRedirect("Administrator");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
