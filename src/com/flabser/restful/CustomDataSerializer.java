package com.flabser.restful;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDataSerializer extends JsonSerializer<Object> {
	
	  public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		    jgen.writeStartObject();
		    jgen.writeObjectField("user", "fsvsdv");
		    jgen.writeEndObject();
		  }

}
