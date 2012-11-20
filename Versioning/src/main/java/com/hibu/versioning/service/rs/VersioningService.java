package com.hibu.versioning.service.rs;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.hibu.versioning.news.CountryContentType;
import com.hibu.versioning.news.CountryContentTypeV1;
import com.hibu.versioning.news.CountryContentTypeV2;


/**
 * @since Nov 19, 2012
 * @author IB0743
 */

@Path("")
public class VersioningService {
	
	
	private static final String JSON_CONTENTTYPE_V1 = "application/vnd.yell.version.versions-v1.1+json";
	private static final String XML_CONTENTTYPE_V1 = "application/vnd.yell.version.versions-v1.1+xml";
	private static final String JSON_CONTENTTYPE_V2 = "application/vnd.yell.version.versions-v2.0+json";
	private static final String XML_CONTENTTYPE_V2 = "application/vnd.yell.version.versions-v2.0+xml";
	private static final String XML_TYPE ="application/xml";
	private static final String JSON_TYPE = "application/json";
	private static final String ACCEPT_TYPES[] =  {JSON_CONTENTTYPE_V1, XML_CONTENTTYPE_V1, JSON_CONTENTTYPE_V2, XML_CONTENTTYPE_V2};
	
	
	
	@Path("country")
	@POST
	@Produces({XML_TYPE, JSON_TYPE, JSON_CONTENTTYPE_V1, XML_CONTENTTYPE_V1, JSON_CONTENTTYPE_V2, XML_CONTENTTYPE_V2})
	public Response createCountryDiffVerObj(@Context HttpHeaders httpHeaders, String requestString) {
		List<String> acceptHeaders = httpHeaders.getRequestHeader("Accept");
		String requestedAcceptType = null;
		boolean isUnknowAcceptHeader = true;
		for(int i=0; i< ACCEPT_TYPES.length; i++) {
			requestedAcceptType = ACCEPT_TYPES[i];
			if(acceptHeaders.contains(requestedAcceptType)) {
				isUnknowAcceptHeader = false;
				break;
			}
		}
		
		if(isUnknowAcceptHeader) {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		
		List<String> contentTypeHeaders = httpHeaders.getRequestHeader("Content-Type");
		String contentType = contentTypeHeaders.get(0);
		
		CountryContentType countryContentType = null;
		if(requestedAcceptType.equals(XML_CONTENTTYPE_V1) || requestedAcceptType.equals(JSON_CONTENTTYPE_V1)) {
			if(!(contentType.equals(XML_CONTENTTYPE_V1) || contentType.equals(JSON_CONTENTTYPE_V1))) {
				return Response.status(Status.BAD_REQUEST).entity("Parser Exception").build();
			}
			countryContentType = getV1CountryContent(requestString, contentType);
		} else {
			if(!(contentType.equals(XML_CONTENTTYPE_V2) || contentType.equals(JSON_CONTENTTYPE_V2))) {
				return Response.status(Status.BAD_REQUEST).entity("Parser Exception").build();
			}
			countryContentType = getV2CountryContent(requestString, contentType);
		}
		
		if(countryContentType.getCountryContentTypeV1() != null) {
			//call the v1 business logic.
		} 
		if( countryContentType.getCountryContentTypeV2() != null) {
			//call the v2 business logic.
		}
		
		return Response.status(Status.CREATED).entity(countryContentType).build();
	}
	
	
	@Path("countryOne")
	@POST
	@Produces({XML_TYPE, JSON_TYPE, JSON_CONTENTTYPE_V1, XML_CONTENTTYPE_V1, JSON_CONTENTTYPE_V2, XML_CONTENTTYPE_V2})
	public Response createCountrySingleObj(@Context HttpHeaders httpHeaders, CountryContentType countryContentType) {
		List<String> acceptHeaders = httpHeaders.getRequestHeader("Accept");
		String requestedAcceptType = null;
		boolean isUnknowAcceptHeader = true;
		for(int i=0; i< ACCEPT_TYPES.length; i++) {
			requestedAcceptType = ACCEPT_TYPES[i];
			if(acceptHeaders.contains(requestedAcceptType)) {
				isUnknowAcceptHeader = false;
				break;
			}
		}
		
		if(isUnknowAcceptHeader) {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		
		if( requestedAcceptType.equals(JSON_CONTENTTYPE_V1) || requestedAcceptType.equals(XML_CONTENTTYPE_V1)) {
			if(countryContentType.getCountryContentTypeV1() == null) {
				return Response.status(Status.BAD_REQUEST).entity("Parser Exception").build();
			}
		} else {
			if(countryContentType.getCountryContentTypeV2() == null) {
				return Response.status(Status.BAD_REQUEST).entity("Parser Exception").build();
			}
		}
		
		if(countryContentType.getCountryContentTypeV1() != null) {
			//call the v1 business logic.
		} 
		if( countryContentType.getCountryContentTypeV2() != null) {
			//call the v2 business logic.
		}
		
		System.out.println("Country cntent Type object is returned "+countryContentType);
		return Response.status(Status.CREATED).entity(countryContentType).build();
	}
	
	/**
     * @param requestString
     * @param requestedAcceptType
     * @return
     */
    private CountryContentType getV2CountryContent(String requestString, String contentType) {
    	CountryContentType countryContentType = new CountryContentType();
     	CountryContentTypeV2 v2 = null;
     	if( contentType.equals(JSON_CONTENTTYPE_V2)) {
     		v2 = (CountryContentTypeV2)convertJsonSringToObject(requestString, CountryContentTypeV2.class);
 	    } else {
 	    	v2 = (CountryContentTypeV2)convertXMLSringToObject(requestString);
 	    }
     	countryContentType.setCountryContentTypeV2(v2);
 	    return countryContentType;
    }



	/**
     * @param requestString
     * @return
     */
    private CountryContentType getV1CountryContent(String requestString, String contentType) {
	    CountryContentType countryContentType = new CountryContentType();
    	CountryContentTypeV1 v1 = null;
    	if( contentType.equals(JSON_CONTENTTYPE_V1)) {
    		v1 = (CountryContentTypeV1)convertJsonSringToObject(requestString, CountryContentTypeV1.class);
	    } else {
	    	v1 = (CountryContentTypeV1)convertXMLSringToObject(requestString);
	    }
    	
    	countryContentType.setCountryContentTypeV1(v1);
	    return countryContentType;
    }



    /**
     * @param requestString
     * @param class1
     * @return
     * @throws JAXBException 
     */
    private Object convertXMLSringToObject(String requestString) {
    	JAXBContext jaxbContext;
    	Object obj = null;
    	try {
    		jaxbContext = JAXBContext.newInstance("com.hibu.versioning.news");
    		Unmarshaller unmarshaller = null;

    		unmarshaller = jaxbContext.createUnmarshaller();
    		obj = (Object)unmarshaller.unmarshal(new StringReader(requestString));
    	} catch (JAXBException e) {
    		e.printStackTrace();
    	}
    	return obj;
    }

	private Object convertJsonSringToObject(String json, Class objClass) {
		StringReader reader = new StringReader(json);
		ObjectMapper mapper  = new ObjectMapper();
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector());
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
		Object contentObject = null;
		try {
			contentObject =  (Object)mapper.readValue(reader, objClass);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentObject;
	}
}
