package ee.enk.rest.jersey.servlet;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import com.sun.jersey.api.json.JSONWithPadding;

import ee.enk.rest.jersey.data.DataProvider;
import ee.enk.rest.jersey.data.NotFoundTradableException;
import ee.enk.rest.jersey.data.Tradable;
import ee.enk.rest.jersey.test.OMXServletTest;

@Path("/omxdata")
public class OMXServlet {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@javax.annotation.security.RolesAllowed({"Visitor"})
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public JSONWithPadding getHandler(@QueryParam("id") String id, @QueryParam("jsoncallback") @DefaultValue("fn") String callback) throws NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundTradableException, InvalidKeyException{	
				
			if(id!=null && id.trim().length()>0){	
				GenericEntity<Tradable> entity = new GenericEntity<Tradable>(getTradable(id)) {};				
				return new JSONWithPadding(entity, callback);
			} 			
		GenericEntity<List<Long>> entity = new GenericEntity<List<Long>>(getAllTradableID()) {};
		return new JSONWithPadding(entity, callback);
		        
    }
	
	@javax.annotation.security.RolesAllowed({"Visitor"})
	@GET	
	@Produces({MediaType.APPLICATION_JSON})
	@Path("{id}")
	public JSONWithPadding getPathHandler(@PathParam("id") String id, @QueryParam("jsoncallback") @DefaultValue("fn") String callback) throws NotFoundTradableException, InvalidKeyException, NoSuchAlgorithmException{
		GenericEntity<Tradable> entity = new GenericEntity<Tradable>(getTradable(id)) {};				
		return new JSONWithPadding(entity, callback);
	}
	
	
	
	@GET	
	@Produces({MediaType.TEXT_PLAIN})
	@Path("/test")
	public String test(){
		return runTests();
	}
	
	
	private Tradable getTradable(String id) throws NotFoundTradableException, InvalidKeyException, NoSuchAlgorithmException{
			return signTradable(DataProvider.instance.getTradable(id));
	}
	
	private List<Long> getAllTradableID() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		List<Long> list = new ArrayList<Long>(DataProvider.instance.getAllTradableId());
		Collections.sort(list);
		
		
		return list; 
	}
	
	private String runTests() {
	    PrintStream sysOut = System.out;
	    PrintStream sysErr = System.err;
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    PrintStream out = new PrintStream(stream);
	    try {
	        System.setOut(out);
	        System.setErr(out);
	        TextListener listener = new TextListener(out);
	        JUnitCore junit = new JUnitCore();
	        junit.addListener(listener);

	        junit.run(OMXServletTest.class);

	    } finally {
	        System.setOut(sysOut);
	        System.setErr(sysErr);
	        out.close();
	    }

	    return stream.toString();
	}
	
	public Tradable signTradable(Tradable t) throws InvalidKeyException, NoSuchAlgorithmException{
		t.setMac(signMac(t.getValuesForMac()));
		return t;
	}
	
	public String signMac(String string) throws NoSuchAlgorithmException, InvalidKeyException{
		SecretKeySpec keySpec = new SecretKeySpec(
		        "qnscAdgRlkIhAUPY44oiexBKtQbGY0orf7OV1I50".getBytes(),
		        "HmacMD5");

		Mac mac = Mac.getInstance("HmacMD5");
		mac.init(keySpec);
		byte[] result = mac.doFinal(string.getBytes());

		//BASE64Encoder encoder = new BASE64Encoder();
		
		//System.out.println(Hex.encodeHexString(result));
		
		return Hex.encodeHexString(result);
	}

}