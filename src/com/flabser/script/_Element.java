package com.flabser.script;

import java.util.ArrayList;

public class _Element implements _IContent {
	private String name;
	private Object value;

	public _Element(String name) {
		this.name = name;
	}

	public _Element(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public _Element(String name, ArrayList<String[]> value) {
		this.name = name;
		this.value = value;
	}
	
	public _Element(String name, int value) {
		this.name = name;
		this.value = Integer.toString(value);
	}
	
	public _Element(String name, _IContent value) {
		this.name = name;
		this.value = value;
	}

	public void addElement(Object e) {
		if (value == null) {
			value = new ArrayList();
		}
		((ArrayList)value).add(e);
	
	}


	public void setValue(String value) {
		this.value =  value;
	}

	public void setValue(int value) {
		this.value = Integer.toString(value);
	}

	@Override
	public StringBuffer toXML() throws _Exception {
		StringBuffer output = new StringBuffer(1000);
		
		if (!name.equalsIgnoreCase(""))	output.append("<" + name + ">");
		
		if (value instanceof _IContent) {
			output.append(((_IContent)value).toXML());
		}else if (value instanceof ArrayList) {
			ArrayList<String[]> list = (ArrayList)value;		
			for (String[] e : list) {
				output.append("<value>");
				for(String strVal : e) {
					output.append("<entry>" + strVal + "</entry>");	
				}
				output.append("</value>");
			}
		}else {
			output.append(value);
		}			
		if (!name.equalsIgnoreCase(""))	output.append("</" + name + ">");
		
		return output;			
			
	}

	public Object getName() {
		return name;
	}
	
	public Object getValue() {
		return value;
	}	

}
