package com.hibu.versioning.service.rs;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.springframework.stereotype.Component;

@Provider
@Component
public class JacksonMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper;

	public JacksonMapperProvider() {
		mapper = new ObjectMapper();
		//mapper.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); //Not WORKING
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector()); //Fix for #208
	}

	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
