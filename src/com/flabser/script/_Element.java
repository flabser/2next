package com.flabser.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class _Element implements _IContent {
	@JsonProperty
	private String name;
	@JsonProperty
	private Object value;

	public _Element(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public _Element(String name, Collection<?> value) {
		this.name = name;
		this.value = value;
	}

	@Deprecated
	public _Element(String name, ArrayList<?> value) {
		this.name = name;
		this.value = value;
	}

	public _Element(String name, Map<?, ?> value) {
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
		((ArrayList) value).add(e);

	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(int value) {
		this.value = Integer.toString(value);
	}

	@Override
	public StringBuffer toXML() throws _Exception {
		StringBuffer output = new StringBuffer(1000);

		if (!name.equalsIgnoreCase("")) {
			output.append("<" + name + ">");
		}

		// System.out.println(value.getClass().getName() + " " + name);
		if (value instanceof _IContent) {
			output.append(((_IContent) value).toXML());
		} else if (value instanceof ArrayList) {
			ArrayList<String[]> list = (ArrayList) value;
			for (Object[] e : list) {
				output.append("<value>");
				for (Object strVal : e) {
					output.append("<entry>" + strVal.toString() + "</entry>");
				}
				output.append("</value>");
			}
		} else if (value instanceof Collection) {
			Collection<_IContent> list = (Collection) value;
			for (_IContent e : list) {
				output.append("<entry>" + e.toXML() + "</entry>");

			}
		} else if (value instanceof Map) {
			Map map = (Map) value;

			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				output.append("<entry key=\"" + pair.getKey() + "\">" + pair.getValue() + "</entry>");
				it.remove();
			}

		} else {
			output.append(value);
		}
		if (!name.equalsIgnoreCase("")) {
			output.append("</" + name + ">");
		}

		return output;

	}

	public Object getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

}
