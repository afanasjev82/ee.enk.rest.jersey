package ee.enk.rest.jersey.auth;

import java.security.Principal;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class OMXSecurityContext implements javax.ws.rs.core.SecurityContext {

	private final User user;
	private final Session session;

	public OMXSecurityContext(Session session, User user) {
		this.session = session;
		this.user = user;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return (null != session) ? session.isSecure() : false;
	}

	@Override
	public boolean isUserInRole(String role) {
				
		if (session == null || !session.isActive()) {
			// Forbidden
			Response denied = Response.status(Response.Status.FORBIDDEN).entity("Permission Denied").build();
			throw new WebApplicationException(denied);
		}

		try {
			// this user has this role?
			return user.getRoles().contains(User.Role.valueOf(role));
		} catch (Exception e) {
		}
		
		return false;
	}
}

