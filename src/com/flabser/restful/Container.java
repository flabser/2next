package com.flabser.restful;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Container {
	private ArrayList data;	
	
	public Container() {	}


	public Container(Object fields) {
		data = (ArrayList) fields;
	}

	@JsonSerialize(using = CustomDataSerializer.class)
	public ArrayList getData() {
		return data;
	}


	public void setData(ArrayList data) {
		this.data = data;
	}
	

}

