package ee.enk.rest.jersey.dao;

public interface GenericDao<T> {
		
    long count();

    T create(T t,String id);

    void delete(String id);

    T findOne(String id);
 
}
