package ee.enk.rest.jersey.dao;

import ee.enk.rest.jersey.auth.User;

public class UserRepository extends Repository<User> {
	
	public final String TYPE = "UserRepository";

	public UserRepository() {
		super();
	}

	@Override
	String getRepositoryType() {
		return TYPE;
	}

}