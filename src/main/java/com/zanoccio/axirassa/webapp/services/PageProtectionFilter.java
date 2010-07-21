
package com.zanoccio.axirassa.webapp.services;

import java.io.IOException;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Response;

public class PageProtectionFilter implements ComponentRequestFilter {
	private final PageRenderLinkSource renderlinksource;
	private final ComponentSource componentsource;
	private final Response response;
	private final AuthenticationService authservice;


	public PageProtectionFilter(PageRenderLinkSource renderlinksource, ComponentSource componentsource,
	        Response response, AuthenticationService authservice) {
		this.renderlinksource = renderlinksource;
		this.componentsource = componentsource;
		this.response = response;
		this.authservice = authservice;
	}


	public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
	        throws IOException {
		if (dispatchedToLoginPage(parameters.getActivePageName())) { return; }

		handler.handleComponentEvent(parameters);
	}


	public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
	        throws IOException {
		if (dispatchedToLoginPage(parameters.getLogicalPageName())) { return; }

		handler.handlePageRender(parameters);
	}


	private boolean dispatchedToLoginPage(String pagename) {
		// if (authservice.isLoggedIn())
		return false;

		// Component page = componentsource.getPage(pagename);
		//
		// if (page.getClass().isAnnotationPresent(PublicPage.class)) { return
		// false; }
		//
		// Link link = renderlinksource.createPageRenderLink("Login");
		// response.sendRedirect(link);
		//
		// return true;
	}
}