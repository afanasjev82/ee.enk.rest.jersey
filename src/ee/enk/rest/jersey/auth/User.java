package ee.enk.rest.jersey.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class User implements java.security.Principal {
	// Role
	public enum Role {Visitor};

	private String userId;          // id
	private Set<Role> roles;        // roles
	
	
	public User(String userId){
		this.userId = userId;
		this.setRoles(new HashSet<Role>(Arrays.asList(Role.Visitor)));
	}
	
	@Override
	public String getName() {
		return null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}

