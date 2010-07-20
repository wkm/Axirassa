
package com.zanoccio.axirassa.webapp.services;

import java.io.IOException;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

public class PageProtectionFilter implements Dispatcher {
	private final static String LOGIN_PAGE = "/login";

	private final PageRenderLinkSource _pagerenderlinksoure;
	private final ComponentSource _componentsource;
	private final Response _response;
	private final ApplicationStateManager _statemanager;
	private final Logger _logger;


	public PageProtectionFilter(PageRenderLinkSource pagerenderlinksource, ComponentSource componentsource,
	        Response response, ApplicationStateManager asm, Logger logger) {

	}


	@Override
	public boolean dispatch(Request request, Response response) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}