package com.flabser.restful;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JavaToJSON {

	public static void main(String[] args) {
		@SuppressWarnings("deprecation")
		HashMap<String, Object> f = new HashMap<String,Object>();
		f.put("login", "it is login");
		f.put("pwd", "taht is password");
		SignIn employee = new SignIn(f);
		//User employee = new User("Lokesh", "Gupta");
	     // Employee employee = new Employee(1, "Lokesh", "Gupta", new Date(1981,8,18));
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.defaultPrettyPrintingWriter().writeValue(new File("c://tmp/user1.json"), employee);
			System.out.println("done...");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
