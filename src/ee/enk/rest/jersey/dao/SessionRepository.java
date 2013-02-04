package ee.enk.rest.jersey.dao;

import ee.enk.rest.jersey.auth.Session;

public class SessionRepository extends Repository<Session> {
	
	public final String TYPE = "SessionRepository";

	public SessionRepository() {
		super();
	}

	@Override
	String getRepositoryType() {
		return TYPE;
	}

}