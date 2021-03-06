package ee.enk.rest.jersey.servlet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ee.enk.rest.jersey.data.TradableErrorBean;

public class NotFoundTradableException extends WebApplicationException {

	private static final long serialVersionUID = -3579691674832467291L;

	public NotFoundTradableException(String message) {
		super(Response.status(Response.Status.NOT_FOUND).
                entity(new TradableErrorBean(message, Response.Status.NOT_FOUND.getStatusCode())).build());		
	}

}
