package ee.enk.rest.jersey.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;


public class OMXServletTest extends JerseyTest {
	public static final String PACKAGE_NAME = "ee.enk.rest.jersey.servlet";
	private WebResource ws;
	
	public OMXServletTest() {
		super(new WebAppDescriptor.Builder(PACKAGE_NAME).build());	
	}
	
	@Test
	@Ignore
	public void testResponse() throws UnsupportedEncodingException {
		/*
		 * When running the app as a web application, it gets exposed at
		 * /omxdata/ URL based on the web.xml.
		 * Grizzly server does not recognize web.xml, so they expose jersey at the container root.
		 */
		//ws = resource().path("rest").path("omxdata");
		ws = resource().path("omxdata");
		ClientResponse response = ws.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assertEquals(200, response.getStatus());
		System.out.println(response.getStatus());
	}
	
	@Test
	public void testGetTradable() throws JSONException{
		ws = resource().path("omxdata").path("97765");
		JSONObject json = ws.get(JSONObject.class);
				
		System.out.println("Running test: get Tradable object using URL ("+ws.getURI()+")");
		
		assertTrue("WS return object type is not "+JSONObject.class.getName(),json instanceof JSONObject);
		assertEquals("Expected object 'highestTradedPrice' property value is wrong!","38",json.get("highestTradedPrice"));
	}
	
	@Test
	public void testGetTradableWithParam() throws JSONException {
		ws = resource().path("omxdata");
		ws = ws.queryParam("id", "97765");
		
		System.out.println("Running test: get Tradable object using @QueryParam ("+ws.getURI()+")");
		
		JSONObject json = ws.get(JSONObject.class);
		
		assertTrue("WS return object type is not "+JSONObject.class.getName(),json instanceof JSONObject);
		assertEquals("Expected object 'bestAsk' property value is wrong!","37.99",json.get("bestAsk"));
	}
	@Test
	public void testTradableNotFound() throws JSONException {
		ws = resource().path("omxdata").path("999");
		
		System.out.println("Running test: get Tradable object that is not exist ("+ws.getURI()+")");
		
		ClientResponse response = ws.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		assertEquals("Expected Resposnse status is 404, but received "+response.getStatus(),404, response.getStatus());
	}
	@Test
	public void testGetTradableWithParamException() throws JSONException {
		ws = resource().path("omxdata");
		ws = ws.queryParam("id", "97765");
		
		System.out.println("Running test: get Tradable object using @QueryParam ("+ws.getURI()+")");
		
		JSONObject json = ws.get(JSONObject.class);
		
		assertEquals("Expected object 'contracts' property value is wrong!","512",json.get("contracts"));
	}
	
}
