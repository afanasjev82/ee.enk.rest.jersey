package ee.enk.rest.jersey.servlet;

import javax.ws.rs.ext.*;
import javax.xml.bind.JAXBContext;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import ee.enk.rest.jersey.data.Tradable;

@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

  private JAXBContext context;
  private Class<?>[] types = { Tradable.class };

  public MyJAXBContextResolver() throws Exception {
    this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
        types);
  }

  public JAXBContext getContext(Class<?> objectType) {
    for (Class<?> c : types) {
      if (c.equals(objectType)) {
        return context;
      }
    }
    return null;
  }
}