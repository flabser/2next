package com.flabser.restful.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Provider
public class ObjectMapperProvider implements ContextResolver <ObjectMapper> {

	private ObjectMapper om;

	public ObjectMapperProvider() {
		om = new ObjectMapper();

		om.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
		om.enable(SerializationFeature.WRAP_ROOT_VALUE);
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		om.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());

	}

	@Override
	public ObjectMapper getContext(@SuppressWarnings("rawtypes") Class type) {
		return om;
	}
}
