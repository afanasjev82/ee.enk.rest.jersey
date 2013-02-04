package ee.enk.rest.jersey.dao;

import java.util.HashMap;

public abstract class Repository<T> implements GenericDao<T> {
	
	private HashMap<String,T> repository;
	
	abstract String getRepositoryType();
	
	public Repository() {
		repository = new HashMap<String,T>();
	}

	@Override
	public long count() {
		return repository.size();
	}

	@Override
	public T create(T t,String sessionId) {
		repository.put(sessionId, t);
		return t;
	}

	@Override
	public void delete(String id) {
		repository.remove(id);
	}

	@Override
	public T findOne(String id) {
		if(repository.containsKey(id))
			return repository.get(id);
		else return null;
	}

	public HashMap<String, T> getRepository() {
		return repository;
	}

	public void setRepository(HashMap<String, T> repository) {
		this.repository = repository;
	}



}
