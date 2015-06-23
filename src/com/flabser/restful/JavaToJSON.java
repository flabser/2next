package com.flabser.restful;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JavaToJSON {
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

	public JavaToJSON(Object o) {
		ObjectMapper mapper = new ObjectMapper();


		try {
			String fileName = o.getClass().getSimpleName() + "_" + timeFormat.format(new Date()) + ".json";
			mapper.defaultPrettyPrintingWriter().writeValue(new File("c://tmp/" + fileName), o);
			System.out.println("done..." + fileName);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("deprecation")
		HashMap<String, Object> f = new HashMap<String, Object>();
		f.put("login", "it is login");
		f.put("pwd", "taht is password");
		//SignIn employee = new SignIn(f);
		// User employee = new User("Lokesh", "Gupta");
		// Employee employee = new Employee(1, "Lokesh", "Gupta", new
		// Date(1981,8,18));



		new JavaToJSON(null);
	}

}
