package ee.enk.rest.jersey.auth;

import java.io.IOException;
import java.util.Iterator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import ee.enk.rest.jersey.dao.SessionRepository;
import ee.enk.rest.jersey.dao.UserRepository;


/**
 * Filter all incoming requests, look for possible session information and use that
 * to create and load a SecurityContext to request. 
 * 
 */
@Component   // let spring manage the lifecycle
@Provider    // register as jersey's provider
public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

	@Autowired
	private SessionRepository sessionRepository;  // DAO to access Session

	@Autowired
	private UserRepository userRepository;  // DAO to access User
	
	@Autowired
	private CredentialStorage usersCredentionals; //allowed user credentionals
	
	
	static int inerationcount;
	
	
	public SessionRepository getSessionRepository() {
		return sessionRepository;
	}

	public void setSessionRepository(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public CredentialStorage getUsersCredentionals() {
		return usersCredentionals;
	}

	public void setUsersCredentionals(CredentialStorage usersCredentionals) {
		this.usersCredentionals = usersCredentionals;
	}

	public static int getInerationcount() {
		return inerationcount;
	}

	public static void setInerationcount(int inerationcount) {
		SecurityContextFilter.inerationcount = inerationcount;
	}

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		
		String sessionId = "";
		
		if(request.getHeaderValue("cookie")!=null && request.getHeaderValue("cookie").length()>0){
			if(request.getHeaderValue("cookie").contains("=")){
				//JSESSIONID=5D2A6587D31CD2E0CBDD2B4B663C6672 => 5D2A6587D31CD2E0CBDD2B4B663C6672
				sessionId = request.getHeaderValue("cookie").split("=")[1];
			}else{
				sessionId = request.getHeaderValue("cookie");
			}
		}
		
				
		//HEADERS info
		MultivaluedMap<String, String> x = request.getRequestHeaders();		
		Iterator<String> i = x.keySet().iterator();
		
		while(i.hasNext()){
			String it = i.next();
			System.out.println(it+": "+x.get(it));
		}	
		System.out.println();


		User user = null;
		Session session = null;

		if (sessionId != null && sessionId.length() > 0) {

			// Load session object from repository
			session = sessionRepository.findOne(sessionId);
			
			// Load associated user from session
			if (session != null) {
				user = userRepository.findOne(session.getUserId());
			}else{ //unauthorized
				// Get Authorization header
			    String auth = request.getHeaderValue("Authorization");
			    
			    String userId = "";
			    try {
			    	userId = allowUser(auth);				
				} catch (IOException e) {
					e.printStackTrace();
					Response denied = Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Server error").build();
					throw new WebApplicationException(denied);			
				}
			    if(userId.length()>0){
			    	//create new user
			    	user = new User(userId);
			    	session = new Session(sessionId, userId);
			    	
			    	sessionRepository.create(session, sessionId);
			    	userRepository.create(user, userId);
			    }else{
			    	//pass again
			    	inerationcount++;
			    	System.out.println("Iteration count:"+inerationcount);
				    Response denied = Response.status(Response.Status.UNAUTHORIZED).entity("Permission Denied").header("WWW-Authenticate", "BASIC realm=\"Restricted Area\"").build();
					throw new WebApplicationException(denied);
			    }
			    

			}
		}
		//Set security context
		request.setSecurityContext(new OMXSecurityContext(session, user));
		return request;

	}

	@Override
	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter() {
		return null;
	}
	
	  // This method checks the user information sent in the Authorization
	  // header against the database of users maintained in the users Hashtable.
	  protected String allowUser(String auth) throws IOException {
	    if (auth == null) return "";  // no auth
	
	    if (!auth.toUpperCase().startsWith("BASIC ")) 
	      return "";  // we only do BASIC
	    
	    // Get encoded user and password, comes after "BASIC "
	    String userpassEncoded = auth.substring(6);
	
	    // Decode it, using any base 64 decoder
	    sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
	    String userpassDecoded = new String(dec.decodeBuffer(userpassEncoded));
    
	    String[] cred = userpassDecoded.split(":");

	    // Check our user list to see if that user and password are "allowed"
	    if (usersCredentionals.containsKey(cred[0]) && usersCredentionals.get(cred[0]).equals(cred[1]))
	      return cred[0];
	    else
	      return "";
	  }
}
