package ee.enk.rest.jersey.auth;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.resource.Singleton;

/**
 * FilterFactory to create List of request/response filters to be applied on a particular
 * AbstractMethod of a resource.
 */
@Singleton
@Component // let spring manage the lifecycle
@Provider  // register as jersey's provider
public class ResourceFilterFactory extends RolesAllowedResourceFilterFactory {
	
	@Autowired
	private SecurityContextFilter securityContextFilter;

	public SecurityContextFilter getSecurityContextFilter() {
		return securityContextFilter;
	}

	public void setSecurityContextFilter(SecurityContextFilter securityContextFilter) {
		this.securityContextFilter = securityContextFilter;
	}
	
	
	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		// get filters from RolesAllowedResourceFilterFactory Factory!
		List<ResourceFilter> rolesFilters = super.create(am);
		if (null == rolesFilters) {
			rolesFilters = new ArrayList<ResourceFilter>();
		}

		// Convert into mutable List, so as to add more filters that we need
		// (RolesAllowedResourceFilterFactory generates immutable list of filters)
		List<ResourceFilter> filters = new ArrayList<ResourceFilter>(rolesFilters);

		// Load SecurityContext first (this will load security context onto request)
		filters.add(0, securityContextFilter);		
		//filters.add(anyMoreFilter?);

		return filters;
	}
}
