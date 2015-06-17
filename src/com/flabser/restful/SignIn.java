package com.flabser.restful;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("signin")
public class SignIn {
	@JsonProperty
	public ArrayList<HashMap> signin = new ArrayList<HashMap>();	
	
	public SignIn() {	}


	public SignIn(HashMap<String,Object> fields) {
		signin.add(fields);
	}
}

